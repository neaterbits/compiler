package com.neaterbits.compiler.resolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.resolver.types.FileSpec;
import com.neaterbits.compiler.resolver.types.IFileImports;
import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.TypeResolveMode;

public final class FilesResolver<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> extends ResolveUtil {

	private final ResolveLogger<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> logger;

	private final Collection<BUILTINTYPE> builtinTypes;
	private final ResolverLibraryTypes<LIBRARYTYPE> libraryTypes;
	
	private final ASTModel<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> astModel;

	private final BuiltinTypesMap<BUILTINTYPE> builtinTypesMap;
	
	public FilesResolver(
			ResolveLogger<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> logger,
			Collection<BUILTINTYPE> builtinTypes,
			ResolverLibraryTypes<LIBRARYTYPE> libraryTypes,
			ASTModel<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> astModel) {

		Objects.requireNonNull(logger);
		Objects.requireNonNull(astModel);

		this.logger = logger;

		this.builtinTypes = builtinTypes;
		this.builtinTypesMap = new BuiltinTypesMap<>(builtinTypes, astModel);

		this.libraryTypes = libraryTypes;
		
		this.astModel = astModel;
	}

	public ResolveFilesResult<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolveFiles(Collection<CompiledFile<COMPLEXTYPE>> allFiles) {

		final ResolvedTypesMap<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolvedTypesMap = new ResolvedTypesMap<>();

		final UnresolvedDependencies unresolvedDependencies = new UnresolvedDependencies();

		final List<ResolvedFile<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolvedFiles = resolveFiles(allFiles, resolvedTypesMap, unresolvedDependencies);
		
		return new ResolveFilesResult<>(resolvedFiles, resolvedTypesMap, builtinTypesMap, builtinTypes, unresolvedDependencies);
	}
	
	private List<ResolvedFile<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolveFiles(
			Collection<CompiledFile<COMPLEXTYPE>> startFiles,
			ResolvedTypesMap<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolvedTypesMap,
			UnresolvedDependencies unresolvedDependencies ) {

		logger.onResolveFilesStart(startFiles);
		
		// final ResolveState resolveState = new ResolveState(startFiles);
		
		final CompiledTypesMap<COMPLEXTYPE> compiledTypesMap = new CompiledTypesMap<>(startFiles);
		
		final List<ResolvedFile<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolvedFiles = new ArrayList<>(startFiles.size());
		
		// Try resolve files while unresolved dependency count has changed between each iteration
		// and there were files resolved
		
		final Set<FileSpec> resolvedFileSpecs = new HashSet<>(startFiles.size());

		for (int count = -1, lastCount = 0; count != lastCount; ) {
			
			for (CompiledFile<COMPLEXTYPE> file : startFiles) {
				
				if (!resolvedFileSpecs.contains(file.getSpec())) {
				
					final List<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolvedTypes = resolveTypes(
							file,
							file.getImports(),
							file.getTypes(),
							compiledTypesMap,
							resolvedTypesMap,
							unresolvedDependencies);
		
					if (resolvedTypes != null) {
						resolvedFiles.add(new ResolvedFileImpl<>(file.getSpec(), resolvedTypes));
						
						resolvedFileSpecs.add(file.getSpec());
					}
				}
			}
			
			lastCount = count;
			count = resolvedFiles.size();
		}
		
		for (ResolvedFile<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolvedFile : resolvedFiles) {
			forEachResolvedTypeNested(resolvedFile.getTypes(), type -> {
				if (type.getDependencies() != null) {
					checkForUpdateOnResolve(type.getDependencies(), compiledTypesMap);
				}
			});
		}
		
		logger.onResolveFilesEnd();
		
		return resolvedFiles;
	}
	
	private void checkForUpdateOnResolve(
			Collection<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> dependencies,
			CompiledTypesMap<COMPLEXTYPE> compiledTypesMap) {
		
		for (ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolvedTypeDependency : dependencies) {

			
			if (resolvedTypeDependency.shouldUpdateOnResolve()) {
			
				final ScopedName scopedName = resolvedTypeDependency.getScopedName();
				
				final CompiledType<COMPLEXTYPE> compiledType = compiledTypesMap.lookupByScopedName(scopedName);

				resolvedTypeDependency.updateOnResolve(compiledType.getType());
			}
		}
			
	}
	
	
	private List<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolveTypes(
			CompiledFile<COMPLEXTYPE> file,
			IFileImports fileImports,
			Collection<CompiledType<COMPLEXTYPE>> types,
			CompiledTypesMap<COMPLEXTYPE> compiledTypesMap,
			ResolvedTypesMap<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolvedTypesMap,
			UnresolvedDependencies unresolvedDependencies) {
		
		boolean resolved = true;
		
		final List<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolvedTypes = new ArrayList<>(types.size());
		
		for (CompiledType<COMPLEXTYPE> type : types) {
			
			final List<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolvedExtendsFrom;
			
			if (type.getExtendsFrom() != null) {
				 resolvedExtendsFrom = resolveTypeDependencies(
						 type.getExtendsFrom(),
						 type.getScopedName(),
						 file,
						 fileImports,
						 compiledTypesMap,
						 unresolvedDependencies);
				 
				 if (resolvedExtendsFrom == null) {
					 resolved = false;
				 }
			}
			else {
				resolvedExtendsFrom = null;
			}
			
			final List<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolvedDependencies;

			if (type.getDependencies() != null) {
				resolvedDependencies = resolveTypeDependencies(
						type.getDependencies(),
						type.getScopedName(),
						file,
						fileImports,
						compiledTypesMap,
						unresolvedDependencies);

				if (resolvedDependencies == null) {
					resolved = false;
				}

			}
			else {
				resolvedDependencies = null;
			}
			
			final List<ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolvedNestedTypes;
			
			if (type.getNestedTypes() != null) {
				 resolvedNestedTypes = resolveTypes(
						 file,
						 fileImports,
						 type.getNestedTypes(),
						 compiledTypesMap,
						 resolvedTypesMap,
						 unresolvedDependencies);
				
				if (resolvedNestedTypes == null) {
					resolved = false;
				}
			}
			else {
				resolvedNestedTypes = null;
			}

			if (!resolved) {
				break;
			}
			
			final ResolvedType<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolvedType = new ResolvedTypeImpl<>(
					file.getSpec(),
					type.getTypeName(),
					type.getSpec().getTypeVariant(),
					type.getType(),
					resolvedNestedTypes,
					resolvedExtendsFrom,
					resolvedDependencies);

			resolvedTypes.add(resolvedType);

			resolvedTypesMap.addMapping(type.getTypeName(), resolvedType);
		}
		
		return resolved ? resolvedTypes : null;
	}
	
	private List<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolveTypeDependencies(
			Collection<CompiledTypeDependency> dependencies,
			ScopedName referencedFrom,
			CompiledFile<COMPLEXTYPE> file,
			IFileImports fileImports,
			CompiledTypesMap<COMPLEXTYPE> compiledTypesMap,
			UnresolvedDependencies unresolvedDependencies) {
		
		boolean resolved = true;
		
		final List<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE>> resolvedTypes = new ArrayList<>(dependencies.size());
		
		for (CompiledTypeDependency compiledTypeDependency : dependencies) {
			
			final ScopedName scopedName = compiledTypeDependency.getScopedName();
			
			final CompiledType<COMPLEXTYPE> foundType = ScopedNameResolver.resolveScopedName(
					scopedName,
					compiledTypeDependency.getReferenceType(),
					fileImports,
					referencedFrom,
					compiledTypesMap);

			
			final ScopedName typeScopedName;
			final TypeName typeName;

			if (foundType != null) {
				typeScopedName = foundType.getScopedName();
				typeName = foundType.getTypeName();
			}
			else {
				
				final BUILTINTYPE builtinType = builtinTypesMap.lookupType(compiledTypeDependency.getScopedName());
				
				if (builtinType != null) {
					typeScopedName = astModel.getBuiltinTypeScopedName(builtinType);
					typeName = astModel.getBuiltinTypeName(builtinType);
				}
				else {
					if (libraryTypes != null) {
						
						final LIBRARYTYPE libraryType = libraryTypes.lookupType(compiledTypeDependency.getScopedName());
						
						if (libraryType != null) {
							typeScopedName = astModel.getLibraryTypeScopedName(libraryType);
							typeName = astModel.getLibraryTypeName(libraryType);
						}
						else {
							typeScopedName = null;
							typeName = null;
						}
					}
					else {
						typeScopedName = null;
						typeName = null;
					}
				}
			}

			if (typeScopedName != null) {

				final TypeResolveMode typeResolveMode = 
						   scopedName.hasScope()
						&& scopedName.scopeStartsWith(typeScopedName.getParts())
						
							? TypeResolveMode.COMPLETE_TO_COMPLETE
							: TypeResolveMode.CLASSNAME_TO_COMPLETE;

				final ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE, LIBRARYTYPE> resolvedTypeDependency;

				resolvedTypeDependency = astModel.makeResolvedTypeDependency(
						typeName,
						compiledTypeDependency.getReferenceType(),
						typeResolveMode,
						null,
						compiledTypeDependency);

				resolvedTypes.add(resolvedTypeDependency);
				unresolvedDependencies.remove(file.getSpec(), compiledTypeDependency);
			}
			else {
				unresolvedDependencies.add(file.getSpec(), compiledTypeDependency);
				
				System.out.println("## Missing dependency " + compiledTypeDependency.getScopedName());

				resolved = false;
			}
		}
		
		return resolved ? resolvedTypes : null;
	}
}
