package com.neaterbits.compiler.common.resolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;

import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.common.loader.CompiledFile;
import com.neaterbits.compiler.common.loader.CompiledType;
import com.neaterbits.compiler.common.loader.CompiledTypeDependency;
import com.neaterbits.compiler.common.loader.FileImports;
import com.neaterbits.compiler.common.loader.ResolvedFile;
import com.neaterbits.compiler.common.loader.ResolvedType;
import com.neaterbits.compiler.common.loader.ResolvedTypeDependency;
import com.neaterbits.compiler.common.loader.ast.TypeResolveMode;
import com.neaterbits.compiler.common.resolver.codemap.CodeMapImpl;
import com.neaterbits.compiler.common.resolver.codemap.ResolvedTypeCodeMapImpl;

public final class FilesResolver {

	private final ResolveLogger logger;
	
	public FilesResolver(ResolveLogger logger) {

		Objects.requireNonNull(logger);
		
		this.logger = logger;
	}

	public ResolveFilesResult resolveFiles(Collection<CompiledFile> allFiles, Collection<? extends BuiltinType> builtinTypes) {

		final ResolvedTypesMap resolvedTypesMap = new ResolvedTypesMap();

		final UnresolvedDependencies unresolvedDependencies = new UnresolvedDependencies();

		final List<ResolvedFile> resolvedFiles = resolveFiles(allFiles, resolvedTypesMap, unresolvedDependencies);
		
		final ResolvedTypeCodeMapImpl codeMap = makeCodeMap(resolvedFiles, builtinTypes);
		
		return new ResolveFilesResult(resolvedFiles, codeMap, resolvedTypesMap, unresolvedDependencies);
	}

	private static ResolvedTypeCodeMapImpl makeCodeMap(List<ResolvedFile> resolvedFiles, Collection<? extends BuiltinType> builtinTypes) {
		
		final ResolvedTypeCodeMapImpl codeMap = new ResolvedTypeCodeMapImpl(new CodeMapImpl(), builtinTypes);
		
		final Map<CompleteName, ResolvedType> resolvedTypesByName = new HashMap<>();
		
		for (ResolvedFile resolvedFile : resolvedFiles) {
			forEachResolvedTypeNested(resolvedFile.getTypes(), type -> resolvedTypesByName.put(type.getCompleteName(), type));
		}
		
		final Set<CompleteName> toAdd = new HashSet<>(resolvedTypesByName.keySet());
		
		// Since types extend from other types, we can only add those were we have added all base classes
		while (!toAdd.isEmpty()) {
			
			final Iterator<CompleteName> iterator = toAdd.iterator();
			
			while (iterator.hasNext()) {

				final CompleteName completeName = iterator.next();
				
				if (codeMap.hasType(completeName)) {
					throw new IllegalStateException();
				}
	
				final ResolvedType type = resolvedTypesByName.get(completeName);
				
				boolean allExtendsFromAdded = true;
				
				if (type.getExtendsFrom() != null) {
					for (ResolvedTypeDependency typeDependency : type.getExtendsFrom()) {
						if (!codeMap.hasType(typeDependency.getCompleteName())) {
							allExtendsFromAdded = false;
							break;
						}
					}
				}
				
				if (allExtendsFromAdded) {
					codeMap.addType(type);
					
					iterator.remove();
				}
			}
		}
		
		final List<Integer> typeNosList = new ArrayList<>();
			
		for (ResolvedFile resolvedFile : resolvedFiles) {
			forEachResolvedTypeNested(resolvedFile.getTypes(), type -> {
				typeNosList.add(codeMap.getTypeNo(type.getCompleteName()));
			});
			
			final int [] typeNos = new int[typeNosList.size()];
			
			for (int i = 0; i < typeNosList.size(); ++ i) {
				typeNos[i] = typeNosList.get(i);
			}
			
			codeMap.addFile(resolvedFile, typeNos);
			
			typeNosList.clear();
		}
		
		final MethodsResolver methodsResolver = new MethodsResolver(codeMap);

		methodsResolver.resolveMethodsForAllTypes(resolvedFiles);
		
		return codeMap;
	}
	
	
	private static void forEachResolvedTypeNested(Collection<ResolvedType> types, Consumer<ResolvedType> forEachType) {
	
		for (ResolvedType type : types) {

			forEachType.accept(type);
			
			if (type.getNestedTypes() != null) {
				forEachResolvedTypeNested(type.getNestedTypes(), forEachType);
			}
		}
	}
	
	private List<ResolvedFile> resolveFiles(Collection<CompiledFile> startFiles, ResolvedTypesMap resolvedTypesMap, UnresolvedDependencies unresolvedDependencies ) {

		logger.onResolveFilesStart(startFiles);
		
		// final ResolveState resolveState = new ResolveState(startFiles);
		
		final CompiledTypesMap compiledTypesMap = new CompiledTypesMap(startFiles);
		
		final List<ResolvedFile> resolvedFiles = new ArrayList<>(startFiles.size());
		
		for (CompiledFile file : startFiles) {
			final List<ResolvedType> resolvedTypes = resolveTypes(file, file.getImports(), file.getTypes(), compiledTypesMap, resolvedTypesMap, unresolvedDependencies);

			if (resolvedTypes != null) {
				resolvedFiles.add(new ResolvedFileImpl(file.getSpec(), resolvedTypes));
			}
		}
		
		logger.onResolveFilesEnd();
		
		return resolvedFiles;
	}
	
	private List<ResolvedType> resolveTypes(
			CompiledFile file,
			FileImports fileImports,
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
			FileImports fileImports,
			CompiledTypesMap compiledTypesMap,
			UnresolvedDependencies unresolvedDependencies) {
		
		boolean resolved = true;
		
		final List<ResolvedTypeDependency> resolvedTypes = new ArrayList<>(dependencies.size());
		
		for (CompiledTypeDependency compiledTypeDependency : dependencies) {
			
			final CompiledType foundType = resolveScopedName(
					compiledTypeDependency.getScopedName(),
					compiledTypeDependency.getReferenceType(),
					fileImports,
					referencedFrom,
					compiledTypesMap);

			if (foundType != null) {
				
				if (compiledTypeDependency.getUpdateOnResolve() != null) {
					
					final TypeResolveMode typeResolveMode = 
							   compiledTypeDependency.getScopedName().hasScope()
							&& compiledTypeDependency.getScopedName().scopeStartsWith(foundType.getType().getCompleteName().toScopedName().getParts())
							
							? TypeResolveMode.COMPLETE_TO_COMPLETE
							: TypeResolveMode.CLASSNAME_TO_COMPLETE;
					
					compiledTypeDependency.getUpdateOnResolve().accept(foundType.getType(), typeResolveMode);
				}
				
				final ResolvedTypeDependency resolvedTypeDependency = new ResolvedTypeDependencyImpl(
						foundType.getCompleteName(),
						compiledTypeDependency.getReferenceType(),
						compiledTypeDependency.getElement());
				
				resolvedTypes.add(resolvedTypeDependency);
			}
			else {
				unresolvedDependencies.add(file.getSpec(), compiledTypeDependency);
				
				resolved = false;
			}
		}
		
		return resolved ? resolvedTypes : null;
	}

	private CompiledType resolveScopedName(ScopedName scopedName, ReferenceType referenceType, FileImports fileImports, ScopedName referencedFrom, CompiledTypesMap compiledTypesMap) {
		
		CompiledType result = resolveScopedName(scopedName, fileImports, referencedFrom, compiledTypesMap);
		
		if (result == null && referenceType == ReferenceType.STATIC_OR_STATIC_INSTANCE_METHOD_CALL) {

			if (scopedName.hasScope() && scopedName.getScope().size() > 1) {
			
				final ScopedName updatedScopeName = new ScopedName(scopedName.getScope().subList(0, scopedName.getScope().size() - 1), scopedName.getName());
				
				result = resolveScopedName(updatedScopeName, fileImports, referencedFrom, compiledTypesMap);
			}
			// Class name as part of scopedName
			else if (scopedName.hasScope() && scopedName.getScope().size() == 1) {

				final ScopedName updatedScopeName = new ScopedName(null, scopedName.getScope().get(0));
				
				result = resolveScopedName(updatedScopeName, fileImports, referencedFrom, compiledTypesMap);
			}
		}
		
		return result;
	}

	private CompiledType resolveScopedName(ScopedName scopedName, FileImports fileImports, ScopedName referencedFrom, CompiledTypesMap compiledTypesMap) {
		
		final CompiledType result;
		final CompiledType inSameNamespace;
		
		if (scopedName.hasScope()) {
			result = compiledTypesMap.lookupByScopedName(scopedName);
		}
		// Check same namespace as reference from
		else if (referencedFrom.hasScope() && (null != (inSameNamespace = compiledTypesMap.lookupByScopedName(new ScopedName(referencedFrom.getScope(), scopedName.getName()))))) {
			result = inSameNamespace;
		}
		// Try imports
		else {
			
			// Only type name, must look at imports
			
			final List<ScopedName> names = fileImports.getAllNameCombinations(scopedName);
			
System.out.println("## name combinations: " + names + " for " + scopedName);
			
			final Map<ScopedName, CompiledType> matches = new HashMap<>();
			
			if (names != null) {
				for (ScopedName name : names) {
					final CompiledType type = compiledTypesMap.lookupByScopedName(name);
					
					if (type != null) {
						// Make sure class-part matches
						if (name.getName().equals(scopedName.getName())) {
							matches.put(name, type);
						}
					}
				}
			}
			
			switch (matches.size()) {
			case 0:
				result = null;
				break;
	
			case 1:
				result = matches.values().iterator().next();
				break;
				
			default:
				throw new IllegalStateException("Multiple matches for " + scopedName);
			}
		}

		return result;
	}
}
