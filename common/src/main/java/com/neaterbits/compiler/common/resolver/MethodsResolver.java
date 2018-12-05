package com.neaterbits.compiler.common.resolver;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import com.neaterbits.compiler.common.ast.block.ClassMethod;
import com.neaterbits.compiler.common.ast.block.Parameter;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.type.NamedType;
import com.neaterbits.compiler.common.ast.type.complex.ClassType;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodModifiers;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodOverride;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodStatic;
import com.neaterbits.compiler.common.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.Subclassing;
import com.neaterbits.compiler.common.loader.ResolvedFile;
import com.neaterbits.compiler.common.loader.ResolvedType;
import com.neaterbits.compiler.common.loader.TypeSpec;
import com.neaterbits.compiler.common.resolver.codemap.MethodVariant;
import com.neaterbits.compiler.common.resolver.codemap.ResolvedTypeCodeMapImpl;

public final class MethodsResolver {

	private final ResolvedTypeCodeMapImpl codeMap;
	
	
	public MethodsResolver(ResolvedTypeCodeMapImpl codeMap) {
		Objects.requireNonNull(codeMap);

		this.codeMap = codeMap;
	}


	void resolveMethodsForAllTypes(Collection<ResolvedFile> allFiles) {
	
		// Create a set of all types and just start resolving one by one
		
		final Map<TypeSpec, ResolvedType> map = new HashMap<>(allFiles.size());
	
		for (ResolvedFile file : allFiles) {
			getAllTypes(file.getTypes(), map);
		}
		
		resolveAllMethods(map);
	}
	
	
	private void getAllTypes(Collection<ResolvedType> types, Map<TypeSpec, ResolvedType> map) {
		
		for (ResolvedType type : types) {

			if (map.put(type.getSpec(), type) != null) {
				throw new IllegalStateException();
			}

			if (type.getNestedTypes() != null) {
				getAllTypes(types, map);
			}
		}
	}
	
	private void resolveAllMethods(Map<TypeSpec, ResolvedType> map) {
		
		final Set<TypeSpec> toResolve = new HashSet<>(map.keySet());

		while (!toResolve.isEmpty()) {

			final TypeSpec typeSpec = toResolve.iterator().next();
			
			resolveAllMethods(map, typeSpec);

			toResolve.remove(typeSpec);
		}
	}
	
	private void resolveAllMethods(Map<TypeSpec, ResolvedType> map, TypeSpec typeSpec) {
	
		Objects.requireNonNull(typeSpec);
		
		final ResolvedType resolvedType = map.get(typeSpec);
		
		addTypeAndMethods(resolvedType);
	}
	
	
	private void addTypeAndMethods(ResolvedType resolvedType) {
		
		Objects.requireNonNull(resolvedType);
		
		// Pass typeNo to references since faster lookup
		final Integer typeNo = codeMap.getTypeNo(resolvedType.getCompleteName());
		
		if (typeNo == null) {
			throw new IllegalArgumentException("No typeNo for " + resolvedType.getCompleteName().getName());
		}
		
		switch (resolvedType.getTypeVariant()) {
		case CLASS:

			final ClassType classType = (ClassType)resolvedType.getType();

			addClassMembers(classType, typeNo);

			// Have added all methods, compute extends from/by
			codeMap.computeMethodExtends(classType.getCompleteName());
			break;
			
		default:
			throw new UnsupportedOperationException();
		}
	}
	
	private void addClassMembers(ClassType classType, int typeNo) {
		
		final Subclassing subclassing = classType.getDefinition().getModifiers().getModifier(Subclassing.class);
		
		int methodIdx = 0;
		
		for (ComplexMemberDefinition memberDefinition : classType.getMembers()) {
			
			if (memberDefinition instanceof ClassMethodMember) {

				final ClassMethodMember classMethodMember = (ClassMethodMember)memberDefinition;
				
				final MethodVariant methodVariant = findMethodVariant(classMethodMember, subclassing);
				
				addClassMethod(typeNo, classMethodMember, methodVariant, methodIdx);
				
				++ methodIdx;
			}
		}
	}
	
	private MethodVariant findMethodVariant(ClassMethodMember classMethodMember, Subclassing subclassing) {

		final ClassMethodModifiers modifiers = classMethodMember.getModifiers();

		final MethodVariant methodVariant;
		
		if (modifiers.hasModifier(ClassMethodStatic.class)) {
			methodVariant = MethodVariant.STATIC;
		}
		else {
			final ClassMethodOverride methodOverride = modifiers.getModifier(ClassMethodOverride.class);
			
			if (methodOverride != null) {
				if (methodOverride == ClassMethodOverride.ABSTRACT) {
					methodVariant = MethodVariant.ABSTRACT;
				}
				else if (methodOverride == ClassMethodOverride.FINAL) {
					methodVariant = MethodVariant.FINAL_IMPLEMENTATION;
				}
				else {
					throw new UnsupportedOperationException();
				}
			}
			else {
				methodVariant = subclassing != null && subclassing == Subclassing.FINAL
						? MethodVariant.FINAL_IMPLEMENTATION
						: MethodVariant.OVERRIDABLE_IMPLEMENTATION;
			}
		}
		
		return methodVariant;
	}
	
	private int addClassMethod(int typeNo, ClassMethodMember classMethodMember, MethodVariant methodVariant, int indexInType) {

		final ClassMethod classMethod = classMethodMember.getMethod();
		
		final CompleteName [] parameterTypes = new CompleteName[classMethod.getParameters().size()];
		
		// final int [] parameterTypes = new int[classMethod.getParameters().size()]; 
		
		int i = 0;
		
		for (Parameter parameter : classMethod.getParameters()) {
			final NamedType namedType = (NamedType)parameter.getType().getType();
			
			parameterTypes[i ++] = namedType.getCompleteName();
		}
		
		final int methodNo = codeMap.addMethod(
				typeNo,
				classMethod.getName().getName(),
				parameterTypes,
				methodVariant,
				indexInType);

		return methodNo;
	}
}
