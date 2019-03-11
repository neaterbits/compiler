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
import com.neaterbits.compiler.util.TypeResolveMode;

public final class FilesResolver<BUILTINTYPE, COMPLEXTYPE> extends ResolveUtil {

	private final ResolveLogger<BUILTINTYPE, COMPLEXTYPE> logger;
	private final ASTModel<BUILTINTYPE, COMPLEXTYPE> astModel;
	
	private final Collection<BUILTINTYPE> builtinTypes;
	private final BuiltinTypesMap<BUILTINTYPE> builtinTypesMap;
	
	public FilesResolver(ResolveLogger<BUILTINTYPE, COMPLEXTYPE> logger, Collection<BUILTINTYPE> builtinTypes, ASTModel<BUILTINTYPE, COMPLEXTYPE> astModel) {

		Objects.requireNonNull(logger);
		Objects.requireNonNull(astModel);
		
		this.logger = logger;
		this.astModel = astModel;
		
		this.builtinTypes = builtinTypes;
		this.builtinTypesMap = new BuiltinTypesMap<>(builtinTypes, astModel);
	}

	public ResolveFilesResult<BUILTINTYPE, COMPLEXTYPE> resolveFiles(Collection<CompiledFile<COMPLEXTYPE>> allFiles) {

		final ResolvedTypesMap<BUILTINTYPE, COMPLEXTYPE> resolvedTypesMap = new ResolvedTypesMap<>();

		final UnresolvedDependencies unresolvedDependencies = new UnresolvedDependencies();

		final List<ResolvedFile<BUILTINTYPE, COMPLEXTYPE>> resolvedFiles = resolveFiles(allFiles, resolvedTypesMap, unresolvedDependencies);
		
		return new ResolveFilesResult<>(resolvedFiles, resolvedTypesMap, builtinTypesMap, builtinTypes, unresolvedDependencies);
	}
	
	private List<ResolvedFile<BUILTINTYPE, COMPLEXTYPE>> resolveFiles(
			Collection<CompiledFile<COMPLEXTYPE>> startFiles,
			ResolvedTypesMap<BUILTINTYPE, COMPLEXTYPE> resolvedTypesMap,
			UnresolvedDependencies unresolvedDependencies ) {

		logger.onResolveFilesStart(startFiles);
		
		// final ResolveState resolveState = new ResolveState(startFiles);
		
		final CompiledTypesMap<COMPLEXTYPE> compiledTypesMap = new CompiledTypesMap<>(startFiles);
		
		final List<ResolvedFile<BUILTINTYPE, COMPLEXTYPE>> resolvedFiles = new ArrayList<>(startFiles.size());
		
		// Try resolve files while unresolved dependency count has changed between each iteration
		// and there were files resolved
		
		final Set<FileSpec> resolvedFileSpecs = new HashSet<>(startFiles.size());

		for (int count = -1, lastCount = 0; count != lastCount; ) {
			
			for (CompiledFile<COMPLEXTYPE> file : startFiles) {
				
				if (!resolvedFileSpecs.contains(file.getSpec())) {
				
					final List<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> resolvedTypes = resolveTypes(
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
		
		for (ResolvedFile<BUILTINTYPE, COMPLEXTYPE> resolvedFile : resolvedFiles) {
			forEachResolvedTypeNested(resolvedFile.getTypes(), type -> {
				if (type.getDependencies() != null) {
					checkForUpdateOnResolve(type.getDependencies(), compiledTypesMap);
				}
			});
		}
		
		logger.onResolveFilesEnd();
		
		return resolvedFiles;
	}
	
	private void checkForUpdateOnResolve(Collection<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE>> dependencies, CompiledTypesMap<COMPLEXTYPE> compiledTypesMap) {
		
		for (ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE> resolvedTypeDependency : dependencies) {

			
			if (resolvedTypeDependency.shouldUpdateOnResolve()) {
			
				final ScopedName scopedName = resolvedTypeDependency.getScopedName();
				
				final CompiledType<COMPLEXTYPE> compiledType = compiledTypesMap.lookupByScopedName(scopedName);

				resolvedTypeDependency.updateOnResolve(compiledType.getType());
			}
		}
			
	}
	
	
	private List<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> resolveTypes(
			CompiledFile<COMPLEXTYPE> file,
			IFileImports fileImports,
			Collection<CompiledType<COMPLEXTYPE>> types,
			CompiledTypesMap<COMPLEXTYPE> compiledTypesMap,
			ResolvedTypesMap<BUILTINTYPE, COMPLEXTYPE> resolvedTypesMap,
			UnresolvedDependencies unresolvedDependencies) {
		
		boolean resolved = true;
		
		final List<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> resolvedTypes = new ArrayList<>(types.size());
		
		for (CompiledType<COMPLEXTYPE> type : types) {
			
			final List<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE>> resolvedExtendsFrom;
			
			if (type.getExtendsFrom() != null) {
				 resolvedExtendsFrom = resolveTypeDependencies(type.getExtendsFrom(), type.getScopedName(), file, fileImports, compiledTypesMap, unresolvedDependencies);
				 
				 if (resolvedExtendsFrom == null) {
					 resolved = false;
				 }
			}
			else {
				resolvedExtendsFrom = null;
			}
			
			final List<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE>> resolvedDependencies;
			
			if (type.getDependencies() != null) {
				resolvedDependencies = resolveTypeDependencies(type.getDependencies(), type.getScopedName(), file, fileImports, compiledTypesMap, unresolvedDependencies);

				if (resolvedDependencies == null) {
					resolved = false;
				}
			}
			else {
				resolvedDependencies = null;
			}
			
			final List<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> resolvedNestedTypes;
			
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
			
			final ResolvedType<BUILTINTYPE, COMPLEXTYPE> resolvedType = new ResolvedTypeImpl<>(
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
	
	private List<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE>> resolveTypeDependencies(
			Collection<CompiledTypeDependency> dependencies,
			ScopedName referencedFrom,
			CompiledFile<COMPLEXTYPE> file,
			IFileImports fileImports,
			CompiledTypesMap<COMPLEXTYPE> compiledTypesMap,
			UnresolvedDependencies unresolvedDependencies) {
		
		boolean resolved = true;
		
		final List<ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE>> resolvedTypes = new ArrayList<>(dependencies.size());
		
		for (CompiledTypeDependency compiledTypeDependency : dependencies) {
			
			final ScopedName scopedName = compiledTypeDependency.getScopedName();
			
			final CompiledType<COMPLEXTYPE> foundType = ScopedNameResolver.resolveScopedName(
					scopedName,
					compiledTypeDependency.getReferenceType(),
					fileImports,
					referencedFrom,
					compiledTypesMap);

			final ResolvedTypeDependency<BUILTINTYPE, COMPLEXTYPE> resolvedTypeDependency;
			
			
			if (foundType != null) {
				
				final ScopedName typeScopedName = foundType.getScopedName();
				
				final TypeResolveMode typeResolveMode = 
						   scopedName.hasScope()
						&& scopedName.scopeStartsWith(typeScopedName.getParts())
						
						? TypeResolveMode.COMPLETE_TO_COMPLETE
						: TypeResolveMode.CLASSNAME_TO_COMPLETE;

				
				resolvedTypeDependency = astModel.makeResolvedTypeDependency(
						foundType.getTypeName(),
						compiledTypeDependency.getReferenceType(),
						typeResolveMode,
						null,
						compiledTypeDependency);
				
			}
			else {
				
				final BUILTINTYPE builtinType = builtinTypesMap.lookupType(compiledTypeDependency.getScopedName());
				
				if (builtinType != null) {
					final TypeResolveMode typeResolveMode = 
							   scopedName.hasScope()
							&& scopedName.scopeStartsWith(astModel.getBuiltinTypeScopedName(builtinType).getParts())
							
							? TypeResolveMode.COMPLETE_TO_COMPLETE
							: TypeResolveMode.CLASSNAME_TO_COMPLETE;

					resolvedTypeDependency = astModel.makeResolvedTypeDependency(
							astModel.getBuiltinTypeName(builtinType),
							compiledTypeDependency.getReferenceType(),
							typeResolveMode,
							null,
							compiledTypeDependency);
				}
				else {
					resolvedTypeDependency = null;
				}
			}

			if (resolvedTypeDependency != null) {
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
