package com.neaterbits.compiler.resolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.neaterbits.compiler.resolver.ast.TypeResolveMode;
import com.neaterbits.compiler.resolver.types.CompiledFile;
import com.neaterbits.compiler.resolver.types.CompiledType;
import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.resolver.types.FileSpec;
import com.neaterbits.compiler.resolver.types.IFileImports;
import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;

public final class FilesResolver extends ResolveUtil {

	private final ResolveLogger logger;
	
	private final Collection<? extends BuiltinType> builtinTypes;
	private final BuiltinTypesMap builtinTypesMap;
	
	public FilesResolver(ResolveLogger logger, Collection<? extends BuiltinType> builtinTypes) {

		Objects.requireNonNull(logger);
		
		this.logger = logger;
		
		this.builtinTypes = builtinTypes;
		this.builtinTypesMap = new BuiltinTypesMap(builtinTypes);
	}

	public ResolveFilesResult resolveFiles(Collection<CompiledFile> allFiles) {

		final ResolvedTypesMap resolvedTypesMap = new ResolvedTypesMap();

		final UnresolvedDependencies unresolvedDependencies = new UnresolvedDependencies();

		final List<ResolvedFile> resolvedFiles = resolveFiles(allFiles, resolvedTypesMap, unresolvedDependencies);
		
		return new ResolveFilesResult(resolvedFiles, resolvedTypesMap, builtinTypesMap, builtinTypes, unresolvedDependencies);
	}
	
	private List<ResolvedFile> resolveFiles(Collection<CompiledFile> startFiles, ResolvedTypesMap resolvedTypesMap, UnresolvedDependencies unresolvedDependencies ) {

		logger.onResolveFilesStart(startFiles);
		
		// final ResolveState resolveState = new ResolveState(startFiles);
		
		final CompiledTypesMap compiledTypesMap = new CompiledTypesMap(startFiles);
		
		final List<ResolvedFile> resolvedFiles = new ArrayList<>(startFiles.size());
		
		// Try resolve files while unresolved dependency count has changed between each iteration
		// and there were files resolved
		
		final Set<FileSpec> resolvedFileSpecs = new HashSet<>(startFiles.size());

		for (int count = -1, lastCount = 0; count != lastCount; ) {
			
			for (CompiledFile file : startFiles) {
				
				if (!resolvedFileSpecs.contains(file.getSpec())) {
				
					final List<ResolvedType> resolvedTypes = resolveTypes(file, file.getImports(), file.getTypes(), compiledTypesMap, resolvedTypesMap, unresolvedDependencies);
		
					if (resolvedTypes != null) {
						resolvedFiles.add(new ResolvedFileImpl(file.getSpec(), resolvedTypes));
						
						resolvedFileSpecs.add(file.getSpec());
					}
				}
			}
			
			lastCount = count;
			count = resolvedFiles.size();
		}
		
		for (ResolvedFile resolvedFile : resolvedFiles) {
			forEachResolvedTypeNested(resolvedFile.getTypes(), type -> {
				if (type.getDependencies() != null) {
					checkForUpdateOnResolve(type.getDependencies(), compiledTypesMap);
				}
			});
		}
		
		logger.onResolveFilesEnd();
		
		return resolvedFiles;
	}
	
	private void checkForUpdateOnResolve(Collection<ResolvedTypeDependency> dependencies, CompiledTypesMap compiledTypesMap) {
		
		for (ResolvedTypeDependency resolvedTypeDependency : dependencies) {

			final ResolvedTypeDependencyImpl impl = (ResolvedTypeDependencyImpl)resolvedTypeDependency;
			
			if (impl.getUpdateOnResolve() != null) {
				
				final CompiledType compiledType = compiledTypesMap.lookupByScopedName(impl.getCompleteName().toScopedName());
				
				impl.getUpdateOnResolve().accept(compiledType.getType(), impl.getTypeResolveMode());
			}
		}
			
	}
	
	
	private List<ResolvedType> resolveTypes(
			CompiledFile file,
			IFileImports fileImports,
			Collection<CompiledType> types,
			CompiledTypesMap compiledTypesMap,
			ResolvedTypesMap resolvedTypesMap,
			UnresolvedDependencies unresolvedDependencies) {
		
		boolean resolved = true;
		
		final List<ResolvedType> resolvedTypes = new ArrayList<>(types.size());
		
		for (CompiledType type : types) {
			
			final List<ResolvedTypeDependency> resolvedExtendsFrom;
			
			if (type.getExtendsFrom() != null) {
				 resolvedExtendsFrom = resolveTypeDependencies(type.getExtendsFrom(), type.getScopedName(), file, fileImports, compiledTypesMap, unresolvedDependencies);
				 
				 if (resolvedExtendsFrom == null) {
					 resolved = false;
				 }
			}
			else {
				resolvedExtendsFrom = null;
			}
			
			final List<ResolvedTypeDependency> resolvedDependencies;
			
			if (type.getDependencies() != null) {
				resolvedDependencies = resolveTypeDependencies(type.getDependencies(), type.getScopedName(), file, fileImports, compiledTypesMap, unresolvedDependencies);

				if (resolvedDependencies == null) {
					resolved = false;
				}
			}
			else {
				resolvedDependencies = null;
			}
			
			final List<ResolvedType> resolvedNestedTypes;
			
			if (type.getNestedTypes() != null) {
				 resolvedNestedTypes = resolveTypes(file, fileImports, type.getNestedTypes(), compiledTypesMap, resolvedTypesMap, unresolvedDependencies);
				
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
			
			final ResolvedType resolvedType = new ResolvedTypeImpl(
					file.getSpec(),
					type.getScopedName(),
					type.getSpec().getTypeVariant(),
					type.getType(),
					resolvedNestedTypes,
					resolvedExtendsFrom,
					resolvedDependencies);

			resolvedTypes.add(resolvedType);

			resolvedTypesMap.addMapping(type.getCompleteName(), resolvedType);
		}
		
		return resolved ? resolvedTypes : null;
	}
	
	private List<ResolvedTypeDependency> resolveTypeDependencies(
			Collection<CompiledTypeDependency> dependencies,
			ScopedName referencedFrom,
			CompiledFile file,
			IFileImports fileImports,
			CompiledTypesMap compiledTypesMap,
			UnresolvedDependencies unresolvedDependencies) {
		
		boolean resolved = true;
		
		final List<ResolvedTypeDependency> resolvedTypes = new ArrayList<>(dependencies.size());
		
		for (CompiledTypeDependency compiledTypeDependency : dependencies) {
			
			final ScopedName scopedName = compiledTypeDependency.getScopedName();
			
			final CompiledType foundType = ScopedNameResolver.resolveScopedName(
					scopedName,
					compiledTypeDependency.getReferenceType(),
					fileImports,
					referencedFrom,
					compiledTypesMap);

			final ResolvedTypeDependency resolvedTypeDependency;
			
			
			if (foundType != null) {
				final TypeResolveMode typeResolveMode = 
						   scopedName.hasScope()
						&& scopedName.scopeStartsWith(foundType.getType().getCompleteName().toScopedName().getParts())
						
						? TypeResolveMode.COMPLETE_TO_COMPLETE
						: TypeResolveMode.CLASSNAME_TO_COMPLETE;

				
				resolvedTypeDependency = new ResolvedTypeDependencyImpl(
						foundType.getCompleteName(),
						compiledTypeDependency.getReferenceType(),
						compiledTypeDependency.getElement(),
						typeResolveMode,
						null,
						compiledTypeDependency.getUpdateOnResolve());
				
			}
			else {
				
				final BuiltinType builtinType = builtinTypesMap.lookupType(compiledTypeDependency.getScopedName());
				
				if (builtinType != null) {
					final TypeResolveMode typeResolveMode = 
							   scopedName.hasScope()
							&& scopedName.scopeStartsWith(builtinType.getCompleteName().toScopedName().getParts())
							
							? TypeResolveMode.COMPLETE_TO_COMPLETE
							: TypeResolveMode.CLASSNAME_TO_COMPLETE;

					resolvedTypeDependency = new ResolvedTypeDependencyImpl(
							builtinType.getCompleteName(),
							compiledTypeDependency.getReferenceType(),
							compiledTypeDependency.getElement(),
							typeResolveMode,
							null,
							compiledTypeDependency.getUpdateOnResolve());
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
