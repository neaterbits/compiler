package com.neaterbits.compiler.common.resolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
import com.neaterbits.compiler.common.resolver.references.References;

public final class FilesResolver {

	private final ResolveLogger logger;
	
	public FilesResolver(ResolveLogger logger) {

		Objects.requireNonNull(logger);
		
		this.logger = logger;
	}

	public ResolveFilesResult resolveFiles(Collection<CompiledFile> allFiles) {

		final References references = new References();
		
		final ResolveState resolveState = resolveFiles(allFiles, references);
		
		return new ResolveFilesResult(references, resolveState);
	}
	
	private ResolveState resolveFiles(Collection<CompiledFile> startFiles, References references) {

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

			final List<ScopedName> allExtendsFrom = new ArrayList<>();
			final List<TypeDependency> allDependencies = new ArrayList<>();
			
			getExtendsFromAndDependencies(fileToResolve.getTypes(), allExtendsFrom, allDependencies);
			
			final FileUnresolvedReferences unresolvedReferences = new FileUnresolvedReferences(fileToResolve, allExtendsFrom, allDependencies);
			final FileCachedResolvedTypes cache = resolveState.getCache(fileSpec);

			tryResolveTypes(fileToResolve, references, resolveState, unresolvedReferences, cache);
			
			tryResolveAnyOtherUnresolvedReferences(fileSpec, references, resolveState);
			
			resolveState.removeFromToProcess(fileSpec);
		}
	
		logger.onResolveFilesEnd();
		
		return resolveState;
	}
	
	private void tryResolveTypes(CompiledFile fileToResolve, References references, ResolveState resolveState, FileUnresolvedReferences unresolvedReferences, FileCachedResolvedTypes cache) {
		
		final FileSpec fileSpec = fileToResolve.getSpec();
		
		final List<ResolvedType> resolvedTypes = resolveTypes(fileToResolve.getTypes(), fileSpec, fileToResolve.getImports(), references, unresolvedReferences, cache);
		
		if (unresolvedReferences.isEmpty()) {
			
			final ResolvedFile resolvedFile = new ResolvedFileImpl(fileSpec, resolvedTypes);

			resolveState.addResolved(resolvedFile);
			
			final int fileNo = references.addFile(resolvedFile);

			for (ResolvedType resolvedType : resolvedTypes) {
				references.addType(fileNo, resolvedType);
			}
		}
		else {
			resolveState.addUnresolvedReferences(fileSpec, unresolvedReferences);
		}
	}
	
	private void tryResolveAnyOtherUnresolvedReferences(FileSpec exceptThisOne, References references, ResolveState resolveState) {
		
		resolveState.forEachUnresolved(fileSpec -> {
			
			if (!fileSpec.equals(exceptThisOne)) {
			
				final FileUnresolvedReferences unresolvedReferences = resolveState.getUnresolvedReferences(fileSpec);
				final FileCachedResolvedTypes cache = resolveState.getCache(fileSpec);
				
				final CompiledFile fileToResolve = unresolvedReferences.getFileToResolve();
				
				tryResolveTypes(fileToResolve, references, resolveState, unresolvedReferences, cache);
			}
		});
	}
	
	private void getExtendsFromAndDependencies(Collection<CompiledType> types, List<ScopedName> extendsFrom, List<TypeDependency> dependencies) {
		
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

	private List<ResolvedType> resolveTypes(Collection<CompiledType> types, FileSpec fileSpec, FileImports fileImports, References references, FileUnresolvedReferences unresolvedReferences, FileCachedResolvedTypes cache) {

		final List<ResolvedType> resolvedTypes = new ArrayList<>(types.size());
		
		for (CompiledType type : types) {

			final ResolvedType resolvedType = resolveTypeDependencies(type, fileSpec, fileImports, references, unresolvedReferences, cache);
			
			if (resolvedType != null) {
				resolvedTypes.add(resolvedType);
			}
		}
		
		return resolvedTypes;
	}
	
	private static final class ResolvedTypeDependencyImpl extends BaseTypeInfo implements ResolvedTypeDependency {

		private final ReferenceType referenceType;
		
		public ResolvedTypeDependencyImpl(ScopedName scopedName, TypeVariant typeVariant, ReferenceType referenceType) {
			super(scopedName, typeVariant);
			
			Objects.requireNonNull(referenceType);

			this.referenceType = referenceType;
		}

		@Override
		public ReferenceType getReferenceType() {
			return referenceType;
		}
	}
	
	private ResolvedType resolveTypeDependencies(CompiledType type, FileSpec fileSpec, FileImports fileImports, References references, FileUnresolvedReferences fileUnresolvedReferences, FileCachedResolvedTypes cache) {

		logger.onResolveType(type);

		final List<ResolvedType> resolvedNestedTypes;
		final List<ResolvedTypeDependency> resolvedExtendsFromList;
		final List<ResolvedTypeDependency> resolvedDependencies;
		
		boolean resolvedOk = true;
		
		if (type.getNestedTypes() != null) {
			
			resolvedNestedTypes = new ArrayList<>(type.getNestedTypes().size());
			
			for (CompiledType nestedType : type.getNestedTypes()) {
				final ResolvedType resolvedNestedType = resolveTypeDependencies(nestedType, fileSpec, fileImports, references, fileUnresolvedReferences, cache);
				
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
			
			for (ScopedName extendsFrom : type.getExtendsFrom()) {
				
				final ResolvedType resolvedExtendsFrom = resolveScopedName(extendsFrom, fileImports, references, cache);

				logger.onTryResolveExtendsFrom(extendsFrom, resolvedExtendsFrom);
				
				if (resolvedExtendsFrom != null) {
					resolvedExtendsFromList.add(new ResolvedTypeDependencyImpl(
							extendsFrom,
							resolvedExtendsFrom.getSpec().getTypeVariant(),
							ReferenceType.EXTENDS_FROM));
					
					fileUnresolvedReferences.removeExtendsFrom(extendsFrom);
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
				
				final ResolvedType resolvedDependency = resolveScopedName(dependency.getScopedName(), fileImports, references, cache);
				
				if (resolvedDependency != null) {
					resolvedExtendsFromList.add(new ResolvedTypeDependencyImpl(
							dependency.getScopedName(),
							resolvedDependency.getSpec().getTypeVariant(),
							dependency.getReferenceType()));
					
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
						resolvedNestedTypes,
						resolvedExtendsFromList,
						resolvedDependencies)
				: null;

		return resolvedType;
	}
	
	private ResolvedType resolveScopedName(ScopedName scopedName, FileImports fileImports, References references, FileCachedResolvedTypes cache) {
		
		final ResolvedType result;

		final ResolvedType cached = cache.get(scopedName);
		
		if (cached != null) {
			result = cached;
		}
		else if (scopedName.hasScope()) {
			result = references.getType(scopedName);
			
			if (result != null) {
				cache.put(scopedName, result);
			}
		}
		else {
			
			// Only type name, must look at imports
			
			final List<ScopedName> names = fileImports.getAllNameCombinations(scopedName);
			
System.out.println("## name combinations: " + names + " for " + scopedName);
			
			final Map<ScopedName, ResolvedType> matches = new HashMap<>();
			
			if (names != null) {
				for (ScopedName name : names) {
					final ResolvedType type = references.getType(name);
					
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
