package com.neaterbits.compiler.resolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.util.BuiltinTypesMap;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.BuiltinTypeRef;

public final class ResolveFilesResult {
	
	private final List<ResolvedFile> resolvedFiles;
	
	private final ResolvedTypesMap resolvedTypesMap;
	private final BuiltinTypesMap builtinTypesMap;
	private final UnresolvedDependencies unresolvedDependencies;
	
	private final Collection<BuiltinTypeRef> builtinTypes;
	
	ResolveFilesResult(
			List<ResolvedFile> resolvedFiles,
			ResolvedTypesMap resolvedTypesMap,
			BuiltinTypesMap builtinTypesMap,
			Collection<BuiltinTypeRef> builtinTypes,
			UnresolvedDependencies unresolvedDependencies) {
		
		Objects.requireNonNull(resolvedFiles);
		
		Objects.requireNonNull(resolvedTypesMap);
		Objects.requireNonNull(builtinTypesMap);
		Objects.requireNonNull(builtinTypes);
		
		Objects.requireNonNull(unresolvedDependencies);
		
		
		this.resolvedFiles = Collections.unmodifiableList(resolvedFiles);
		
		// this.resolveState = resolveState;
		this.resolvedTypesMap = resolvedTypesMap;
		this.builtinTypesMap = builtinTypesMap;
		this.unresolvedDependencies = unresolvedDependencies;
		
		this.builtinTypes = builtinTypes;
	}

	public List<ResolvedFile> getResolvedFiles() {
		return resolvedFiles;
	}

	
	public ResolvedTypesMap getResolvedTypesMap() {
		return resolvedTypesMap;
	}
	
	public BuiltinTypesMap getBuiltinTypesMap() {
		return builtinTypesMap;
	}

	public ResolvedType getType(TypeName completeName) {
		
		Objects.requireNonNull(completeName);
		
		return resolvedTypesMap.lookupType(completeName);
	}

	public UnresolvedDependencies getUnresolvedDependencies() {
		return unresolvedDependencies;
	}

	public Collection<BuiltinTypeRef> getBuiltinTypes() {
		return builtinTypes;
	}
	
	public Map<FileSpec, List<ResolveError>> getResolveErrors() {

		final UnresolvedDependencies unresolved = getUnresolvedDependencies();

		final Map<FileSpec, List<ResolveError>> resolveErrors;
   
		if (!unresolved.isEmpty()) {
			resolveErrors = new HashMap<>(unresolved.getCount());
           
			unresolved.forEach((fileSpec, compiledTypeDependency) -> {
                   
				List<ResolveError> errors = resolveErrors.get(fileSpec);
				
				if (errors == null) {
					errors = new ArrayList<>();

					resolveErrors.put(fileSpec, errors);
				}

				errors.add(makeResolveError(compiledTypeDependency));
			});
		}
		else {
           resolveErrors = Collections.emptyMap();
	   	}

		return resolveErrors;
	}
	
	private static ResolveError makeResolveError(CompiledTypeDependency compiledTypeDependency) {
		
		final ResolveError resolveError = new ResolveError("Cannot resolve name " + 
				compiledTypeDependency.getScopedName().toString());

		return resolveError;
	}
	
	public List<ResolveError> getResolveErrors(FileSpec fileSpec) {
		
		final UnresolvedDependencies unresolved = getUnresolvedDependencies();

		final List<ResolveError> resolveErrors;
   
		if (!unresolved.isEmpty()) {
			resolveErrors = new ArrayList<>();
           
			unresolved.forEach((spec, compiledTypeDependency) -> {
                   
				if (spec.equals(fileSpec)) {
					resolveErrors.add(makeResolveError(compiledTypeDependency));
				}
			});
		}
		else {
           resolveErrors = Collections.emptyList();
	   	}

		return resolveErrors;
	}

		
	/*
	Set<CompiledTypeDependency> getUnresolvedExtendsFrom(FileSpec fileSpec) {
		Objects.requireNonNull(fileSpec);
		
		if (!resolveState.hasFile(fileSpec)) {
			throw new IllegalArgumentException("File not resolved " + fileSpec);
		}
		
		return resolveState.getUnresolvedExtendsFrom(fileSpec);
	}

	Set<CompiledTypeDependency> getUnresolvedTypeDependencies(FileSpec fileSpec) {
		Objects.requireNonNull(fileSpec);
		
		if (!resolveState.hasFile(fileSpec)) {
			throw new IllegalArgumentException("File not resolved " + fileSpec);
		}
		
		return resolveState.getUnresolvedDependencies(fileSpec);
	}
	
	public Map<FileSpec, Set<CompiledTypeDependency>> getUnresolvedDependencies() {
		return resolveState.getAllUnresolvedDependencies();
	}

	public Collection<ResolvedFile> getResolvedFiles() {
		return resolveState.getResolvedFiles();
	}
	*/
}
