package com.neaterbits.compiler.common.resolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.loader.BaseTypeInfo;
import com.neaterbits.compiler.common.loader.CompiledFile;
import com.neaterbits.compiler.common.loader.CompiledType;
import com.neaterbits.compiler.common.loader.FileImports;
import com.neaterbits.compiler.common.loader.FileSpec;
import com.neaterbits.compiler.common.loader.ResolvedFile;
import com.neaterbits.compiler.common.loader.ResolvedType;
import com.neaterbits.compiler.common.loader.ResolvedTypeDependency;
import com.neaterbits.compiler.common.loader.TypeDependency;
import com.neaterbits.compiler.common.loader.TypeVariant;
import com.neaterbits.compiler.common.resolver.codemap.CodeMapImpl;
import com.neaterbits.compiler.common.resolver.codemap.ResolvedTypeCodeMapImpl;

public final class FilesResolver {

	private final ResolveLogger logger;
	
	public FilesResolver(ResolveLogger logger) {

		Objects.requireNonNull(logger);
		
		this.logger = logger;
	}

	public ResolveFilesResult resolveFiles(Collection<CompiledFile> allFiles) {

		final ResolvedTypeCodeMapImpl codeMap = new ResolvedTypeCodeMapImpl(new CodeMapImpl());
		
		final ResolveState resolveState = resolveFiles(allFiles, codeMap);
		
		final MethodsResolver methodsResolver = new MethodsResolver(codeMap);

		methodsResolver.resolveMethodsForAllTypes(resolveState.getResolvedFiles());
		
		return new ResolveFilesResult(codeMap, resolveState);
	}
	
	private ResolveState resolveFiles(Collection<CompiledFile> startFiles, ResolvedTypeCodeMapImpl codeMap) {

		logger.onResolveFilesStart(startFiles);
		
		final ResolveState resolveState = new ResolveState(startFiles);
		
		while (resolveState.hasLeftToProcess()) {

			final CompiledFile fileToResolve = resolveState.getNextToProcess();
			
			logger.onResolving(fileToResolve);
			
			final FileSpec fileSpec = fileToResolve.getSpec();
			
			if (resolveState.hasUnresolvedFile(fileSpec)) {
				throw new IllegalStateException();
			}

			if (resolveState.isResolved(fileSpec)) {
				throw new IllegalStateException();
			}

			final List<TypeDependency> allExtendsFrom = new ArrayList<>();
			final List<TypeDependency> allDependencies = new ArrayList<>();
			
			getExtendsFromAndDependencies(fileToResolve.getTypes(), allExtendsFrom, allDependencies);
			
			final FileUnresolvedReferences unresolvedReferences = new FileUnresolvedReferences(fileToResolve, allExtendsFrom, allDependencies);
			final FileCachedResolvedTypes cache = resolveState.getCache(fileSpec);

			tryResolveTypes(fileToResolve, codeMap, resolveState, unresolvedReferences, cache);
			
			tryResolveAnyOtherUnresolvedReferences(fileSpec, codeMap, resolveState);
			
			resolveState.removeFromToProcess(fileSpec);
		}
	
		logger.onResolveFilesEnd();
		
		return resolveState;
	}
	
	private void tryResolveTypes(
			CompiledFile fileToResolve,
			ResolvedTypeCodeMapImpl codeMap,
			ResolveState resolveState,
			FileUnresolvedReferences unresolvedReferences,
			FileCachedResolvedTypes cache) {
		
		final FileSpec fileSpec = fileToResolve.getSpec();
		
		final List<ResolvedType> resolvedTypes = resolveTypes(fileToResolve.getTypes(), fileSpec, fileToResolve.getImports(), codeMap, unresolvedReferences, cache);
		
		if (unresolvedReferences.isEmpty()) {
			
			final ResolvedFile resolvedFile = new ResolvedFileImpl(fileSpec, resolvedTypes);

			resolveState.addResolved(resolvedFile);
			
			final int [] typeNos = new int[resolvedTypes.size()];
			
			int dstIdx = 0;
			
			for (ResolvedType resolvedType : resolvedTypes) {
				final int typeNo = codeMap.addType(resolvedType);

				typeNos[dstIdx ++] = typeNo;
			}
			
			codeMap.addFile(resolvedFile, typeNos);
		}
		else {
			resolveState.addUnresolvedReferences(fileSpec, unresolvedReferences);
		}
	}
	
	
	private void tryResolveAnyOtherUnresolvedReferences(FileSpec exceptThisOne, ResolvedTypeCodeMapImpl codeMap, ResolveState resolveState) {
		
		
		resolveState.forEachUnresolved(fileSpec -> {
			
			if (!fileSpec.equals(exceptThisOne)) {
			
				final FileUnresolvedReferences unresolvedReferences = resolveState.getUnresolvedReferences(fileSpec);
				final FileCachedResolvedTypes cache = resolveState.getCache(fileSpec);
				
				final CompiledFile fileToResolve = unresolvedReferences.getFileToResolve();
				
				tryResolveTypes(fileToResolve, codeMap, resolveState, unresolvedReferences, cache);
			}
		});
	}
	
	private void getExtendsFromAndDependencies(Collection<CompiledType> types, List<TypeDependency> extendsFrom, List<TypeDependency> dependencies) {
		
		for (CompiledType type : types) {
			if (type.getExtendsFrom() != null) {
				extendsFrom.addAll(type.getExtendsFrom());
			}
			
			if (type.getDependencies() != null) {
				dependencies.addAll(type.getDependencies());
			}
			
			if (type.getNestedTypes() != null) {
				getExtendsFromAndDependencies(type.getNestedTypes(), extendsFrom, dependencies);
			}
		}
	}

	private List<ResolvedType> resolveTypes(Collection<CompiledType> types, FileSpec fileSpec, FileImports fileImports, ResolvedTypeCodeMapImpl codeMap, FileUnresolvedReferences unresolvedReferences, FileCachedResolvedTypes cache) {

		final List<ResolvedType> resolvedTypes = new ArrayList<>(types.size());
		
		for (CompiledType type : types) {

			final ResolvedType resolvedType = resolveTypeDependencies(type, fileSpec, fileImports, codeMap, unresolvedReferences, cache);
			
			if (resolvedType != null) {
				resolvedTypes.add(resolvedType);
			}
		}
		
		return resolvedTypes;
	}
	
	
	
	private static final class ResolvedTypeDependencyImpl extends BaseTypeInfo implements ResolvedTypeDependency {

		private final ReferenceType referenceType;
		private final TypeReference element;
		private final ResolvedType resolvedType;
		
		public ResolvedTypeDependencyImpl(ScopedName scopedName, TypeVariant typeVariant, ReferenceType referenceType, TypeReference element, ResolvedType resolvedType) {
			super(scopedName, typeVariant);
			
			Objects.requireNonNull(referenceType);
			Objects.requireNonNull(element);
			Objects.requireNonNull(resolvedType);

			this.referenceType = referenceType;
			this.element = element;
			this.resolvedType = resolvedType;
		}

		@Override
		public ReferenceType getReferenceType() {
			return referenceType;
		}

		@Override
		public TypeReference getElement() {
			return element;
		}

		@Override
		public ResolvedType getResolvedType() {
			return resolvedType;
		}
	}
	
	private ResolvedType resolveTypeDependencies(CompiledType type, FileSpec fileSpec, FileImports fileImports, ResolvedTypeCodeMapImpl codeMap, FileUnresolvedReferences fileUnresolvedReferences, FileCachedResolvedTypes cache) {

		logger.onResolveTypeStart(type);

		final List<ResolvedType> resolvedNestedTypes;
		final List<ResolvedTypeDependency> resolvedExtendsFromList;
		final List<ResolvedTypeDependency> resolvedDependencies;
		
		boolean resolvedOk = true;
		
		if (type.getNestedTypes() != null) {
			
			resolvedNestedTypes = new ArrayList<>(type.getNestedTypes().size());
			
			for (CompiledType nestedType : type.getNestedTypes()) {
				final ResolvedType resolvedNestedType = resolveTypeDependencies(nestedType, fileSpec, fileImports, codeMap, fileUnresolvedReferences, cache);
				
				if (resolvedNestedType != null) {
					resolvedNestedTypes.add(resolvedNestedType);
				}
				else {
					resolvedOk = false;
				}
			}
		}
		else {
			resolvedNestedTypes = null;
		}
		
		if (type.getExtendsFrom() != null) {
			
			resolvedExtendsFromList = new ArrayList<>(type.getExtendsFrom().size());
			
			for (TypeDependency extendsFrom : type.getExtendsFrom()) {
				
				final ResolvedType resolvedExtendsFrom = resolveScopedName(extendsFrom.getScopedName(), fileImports, type.getScopedName(), codeMap, cache);

				logger.onTryResolveExtendsFrom(extendsFrom.getScopedName(), resolvedExtendsFrom);
				
				if (resolvedExtendsFrom != null) {
					resolvedExtendsFromList.add(new ResolvedTypeDependencyImpl(
							extendsFrom.getScopedName(),
							resolvedExtendsFrom.getSpec().getTypeVariant(),
							ReferenceType.EXTENDS_FROM,
							extendsFrom.getElement(),
							resolvedExtendsFrom));
					
					fileUnresolvedReferences.removeExtendsFrom(extendsFrom.getScopedName());
				}
				else {
					resolvedOk = false;
				}
			}
		}
		else {
			resolvedExtendsFromList = null;
		}
		
		if (type.getDependencies() != null) {
			resolvedDependencies = new ArrayList<>(type.getDependencies().size());
			
			for (TypeDependency dependency : type.getDependencies()) {

				final ResolvedType resolvedDependency = resolveScopedName(dependency.getScopedName(), dependency.getReferenceType(), fileImports, type.getScopedName(), codeMap, cache);

				logger.onResolveTypeDependency(dependency, resolvedDependency);

				
				if (resolvedDependency != null) {
					resolvedDependencies.add(new ResolvedTypeDependencyImpl(
							dependency.getScopedName(),
							resolvedDependency.getSpec().getTypeVariant(),
							dependency.getReferenceType(),
							dependency.getElement(),
							resolvedDependency));
					
					fileUnresolvedReferences.removeDependency(dependency);
				}
				else {
					resolvedOk = false;
				}
			}
		}
		else {
			resolvedDependencies = null;
		}
		
		final ResolvedType resolvedType = resolvedOk
				? new ResolvedTypeImpl(
						fileSpec,
						type.getScopedName(),
						type.getSpec().getTypeVariant(),
						type.getType(),
						resolvedNestedTypes,
						resolvedExtendsFromList,
						resolvedDependencies)
				: null;

		logger.onResolveTypeEnd(resolvedType);
						
		return resolvedType;
	}

	private ResolvedType resolveScopedName(ScopedName scopedName, ReferenceType referenceType, FileImports fileImports, ScopedName referencedFrom, ResolvedTypeCodeMapImpl codeMap, FileCachedResolvedTypes cache) {
		
		ResolvedType result = resolveScopedName(scopedName, fileImports, referencedFrom, codeMap, cache);
		
		if (result == null && referenceType == ReferenceType.STATIC_OR_STATIC_INSTANCE_METHOD_CALL) {

			if (scopedName.hasScope() && scopedName.getScope().size() > 1) {
			
				final ScopedName updatedScopeName = new ScopedName(scopedName.getScope().subList(0, scopedName.getScope().size() - 1), scopedName.getName());
				
				result = resolveScopedName(
						updatedScopeName,
						fileImports,
						referencedFrom,
						codeMap,
						cache);
			}
			// Class name as part of scopedName
			else if (scopedName.hasScope() && scopedName.getScope().size() == 1) {

				final ScopedName updatedScopeName = new ScopedName(null, scopedName.getScope().get(0));
				
				result = resolveScopedName(
						updatedScopeName,
						fileImports,
						referencedFrom,
						codeMap,
						cache);
			}
		}
		
		return result;
	}

	private ResolvedType resolveScopedName(ScopedName scopedName, FileImports fileImports, ScopedName referencedFrom, ResolvedTypeCodeMapImpl codeMap, FileCachedResolvedTypes cache) {
		
		final ResolvedType result;

		final ResolvedType cached = cache.get(scopedName);
		
		final ResolvedType inSameNamespace;
		
		// Resolved and cached?
		if (cached != null) {
			result = cached;
		}
		// If has scope, try to look up in codeMap
		else if (scopedName.hasScope()) {
			result = codeMap.getType(scopedName);
			
			if (result != null) {
				cache.put(scopedName, result);
			}
		}
		// Check same namespace as reference from
		else if (referencedFrom.hasScope() && (null != (inSameNamespace = codeMap.getType(new ScopedName(referencedFrom.getScope(), scopedName.getName()))))) {
			
			result = inSameNamespace;

			cache.put(scopedName, result);
		}
		// Try imports
		else {
			
			// Only type name, must look at imports
			
			final List<ScopedName> names = fileImports.getAllNameCombinations(scopedName);
			
System.out.println("## name combinations: " + names + " for " + scopedName);
			
			final Map<ScopedName, ResolvedType> matches = new HashMap<>();
			
			if (names != null) {
				for (ScopedName name : names) {
					final ResolvedType type = codeMap.getType(name);
					
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
		
			if (result != null) {
				cache.put(scopedName, result);
			}
		}

		return result;
	}
}
