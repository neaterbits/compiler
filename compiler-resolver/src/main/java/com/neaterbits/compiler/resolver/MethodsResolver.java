package com.neaterbits.compiler.resolver;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.neaterbits.compiler.codemap.MethodVariant;
import com.neaterbits.compiler.resolver.types.ResolvedFile;
import com.neaterbits.compiler.resolver.types.ResolvedType;
import com.neaterbits.compiler.resolver.types.TypeSpec;
import com.neaterbits.compiler.util.TypeName;

public final class MethodsResolver<BUILTINTYPE, COMPLEXTYPE> {

	private final ResolvedTypeCodeMapImpl<BUILTINTYPE, COMPLEXTYPE> codeMap;
	private final ASTModel<BUILTINTYPE, COMPLEXTYPE> astModel;
	
	public MethodsResolver(ResolvedTypeCodeMapImpl<BUILTINTYPE, COMPLEXTYPE> codeMap, ASTModel<BUILTINTYPE, COMPLEXTYPE> astModel) {

		Objects.requireNonNull(codeMap);
		Objects.requireNonNull(astModel);

		this.codeMap = codeMap;
		this.astModel = astModel;
	}


	void resolveMethodsForAllTypes(Collection<ResolvedFile<BUILTINTYPE, COMPLEXTYPE>> allFiles) {
	
		// Create a set of all types and just start resolving one by one
		
		final Map<TypeSpec, ResolvedType<BUILTINTYPE, COMPLEXTYPE>> map = new HashMap<>(allFiles.size());
	
		for (ResolvedFile<BUILTINTYPE, COMPLEXTYPE> file : allFiles) {
			getAllTypes(file.getTypes(), map);
		}
		
		resolveAllMethods(map);
	}
	
	
	private void getAllTypes(Collection<ResolvedType<BUILTINTYPE, COMPLEXTYPE>> types, Map<TypeSpec, ResolvedType<BUILTINTYPE, COMPLEXTYPE>> map) {
		
		for (ResolvedType<BUILTINTYPE, COMPLEXTYPE> type : types) {

			if (map.put(type.getSpec(), type) != null) {
				throw new IllegalStateException();
			}

			if (type.getNestedTypes() != null) {
				getAllTypes(types, map);
			}
		}
	}
	
	private void resolveAllMethods(Map<TypeSpec, ResolvedType<BUILTINTYPE, COMPLEXTYPE>> map) {
		
		final Set<TypeSpec> toResolve = new HashSet<>(map.keySet());

		while (!toResolve.isEmpty()) {

			final TypeSpec typeSpec = toResolve.iterator().next();
			
			resolveAllMethods(map, typeSpec);

			toResolve.remove(typeSpec);
		}
	}
	
	private void resolveAllMethods(Map<TypeSpec, ResolvedType<BUILTINTYPE, COMPLEXTYPE>> map, TypeSpec typeSpec) {
	
		Objects.requireNonNull(typeSpec);
		
		final ResolvedType<BUILTINTYPE, COMPLEXTYPE> resolvedType = map.get(typeSpec);
		
		addTypeAndMethods(resolvedType);
	}
	
	
	private void addTypeAndMethods(ResolvedType<BUILTINTYPE, COMPLEXTYPE> resolvedType) {
		
		Objects.requireNonNull(resolvedType);
		
		// Pass typeNo to references since faster lookup
		
		final Integer typeNo = codeMap.getTypeNo(resolvedType.getTypeName());
		
		if (typeNo == null) {
			throw new IllegalArgumentException("No typeNo for " + resolvedType.getTypeName().toDebugString());
		}
		
		switch (resolvedType.getTypeVariant()) {
		case CLASS:

			addClassMembers(resolvedType.getType(), typeNo);

			// Have added all methods, compute extends from/by
			codeMap.computeMethodExtends(resolvedType.getTypeName());
			break;
			
		default:
			throw new UnsupportedOperationException();
		}
	}
	
	private void addClassMembers(COMPLEXTYPE classType, int typeNo) {
		
		astModel.iterateClassMethods(classType, (name, methodVariant, returnType, parameterTypes, indexInType) -> {
			
			addClassMethod(typeNo, name, methodVariant, parameterTypes, indexInType);
		});
	}
	
	private int addClassMethod(
			int typeNo,
			String name,
			MethodVariant methodVariant,
			TypeName [] parameterTypes,
			int indexInType) {
		
		final int methodNo = codeMap.addMethod(
				typeNo,
				name,
				parameterTypes,
				methodVariant,
				indexInType);

		return methodNo;
	}
}
