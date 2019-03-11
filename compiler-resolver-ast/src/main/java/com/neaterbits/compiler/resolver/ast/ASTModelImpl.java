package com.neaterbits.compiler.resolver.ast;

import com.neaterbits.compiler.ast.block.ClassMethod;
import com.neaterbits.compiler.ast.block.Parameter;
import com.neaterbits.compiler.ast.type.NamedType;
import com.neaterbits.compiler.ast.type.complex.ClassType;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodModifiers;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodOverride;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodStatic;
import com.neaterbits.compiler.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.ast.typedefinition.Subclassing;
import com.neaterbits.compiler.codemap.MethodVariant;
import com.neaterbits.compiler.codemap.TypeVariant;
import com.neaterbits.compiler.resolver.ASTMethodVisitor;
import com.neaterbits.compiler.resolver.ASTModel;
import com.neaterbits.compiler.resolver.ReferenceType;
import com.neaterbits.compiler.resolver.types.CompiledTypeDependency;
import com.neaterbits.compiler.resolver.types.ResolvedTypeDependency;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.TypeResolveMode;

public class ASTModelImpl implements ASTModel<BuiltinType, ComplexType<?, ?, ?>> {

	@Override
	public ScopedName getBuiltinTypeScopedName(BuiltinType builtinType) {
		return builtinType.getCompleteName().toScopedName();
	}

	@Override
	public String getBuiltinTypeNameString(BuiltinType builtinType) {
		return builtinType.getName().getName();
	}

	@Override
	public TypeName getBuiltinTypeName(BuiltinType builtinType) {
		return builtinType.getCompleteName().toTypeName();
	}

	@Override
	public ResolvedTypeDependency<BuiltinType, ComplexType<?, ?, ?>> makeResolvedTypeDependency(
			TypeName completeName,
			ReferenceType referenceType,
			TypeResolveMode typeResolveMode,
			TypeVariant typeVariant,
			CompiledTypeDependency compiledTypeDependency) {
		
		final ParsedTypeReference parsedTypeReference = (ParsedTypeReference)compiledTypeDependency;
		
		return new ResolvedTypeDependencyImpl(
				completeName,
				referenceType,
				parsedTypeReference.getElement(),
				typeResolveMode,
				typeVariant,
				parsedTypeReference.getUpdateOnResolve());
	}

	@Override
	public int getNumMethods(ComplexType<?, ?, ?> complextype) {
		int numMethods = 0;
		
		if (complextype != null && complextype.getMembers() != null) {
		
			for (ComplexMemberDefinition member : complextype.getMembers()) {
				if (member.isMethod()) {
					++ numMethods;
				}
			}
		}
		
		return numMethods;
	}

	@Override
	public void iterateClassMethods(ComplexType<?, ?, ?> complexType, ASTMethodVisitor visitor) {
		iterateClassMembers((ClassType)complexType, visitor);
	}

	private void iterateClassMembers(ClassType complexType, ASTMethodVisitor visitor) {
		
		final Subclassing subclassing = complexType.getDefinition().getModifiers().getModifier(Subclassing.class);
		
		int methodIdx = 0;
		
		for (ComplexMemberDefinition memberDefinition : complexType.getMembers()) {
			
			if (memberDefinition instanceof ClassMethodMember) {

				final ClassMethodMember classMethodMember = (ClassMethodMember)memberDefinition;
				
				final MethodVariant methodVariant = findMethodVariant(classMethodMember, subclassing);
				
				visitClassMethod(classMethodMember, methodVariant, methodIdx, visitor);
				
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
	
	private void visitClassMethod(ClassMethodMember classMethodMember, MethodVariant methodVariant, int indexInType, ASTMethodVisitor visitor) {

		final ClassMethod classMethod = classMethodMember.getMethod();
		
		final TypeName [] parameterTypes = new TypeName[classMethod.getParameters().size()];
		
		// final int [] parameterTypes = new int[classMethod.getParameters().size()]; 
		
		int i = 0;
		
		for (Parameter parameter : classMethod.getParameters()) {
			final NamedType namedType = (NamedType)parameter.getType().getType();
			
			parameterTypes[i ++] = namedType.getCompleteName().toTypeName();
		}
		
		final NamedType returnType = (NamedType)classMethod.getReturnType().getType();
		
		visitor.onMethod(
				classMethod.getName().getName(),
				methodVariant,
				returnType.getCompleteName().toTypeName(),
				parameterTypes,
				indexInType);
	}
}
