package com.neaterbits.compiler.java.bytecode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import com.neaterbits.compiler.bytecode.common.ClassBytecodeTyped;
import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ResolveLaterTypeReference;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.block.ClassMethod;
import com.neaterbits.compiler.common.ast.block.Parameter;
import com.neaterbits.compiler.common.ast.block.ParameterName;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.type.BaseTypeName;
import com.neaterbits.compiler.common.ast.type.complex.ClassType;
import com.neaterbits.compiler.common.ast.type.complex.InterfaceType;
import com.neaterbits.compiler.common.ast.type.complex.InvocableType;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodModifierHolder;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodModifiers;
import com.neaterbits.compiler.common.ast.typedefinition.ClassModifierHolder;
import com.neaterbits.compiler.common.ast.typedefinition.ClassModifiers;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;
import com.neaterbits.compiler.common.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.FieldModifierHolder;
import com.neaterbits.compiler.common.ast.typedefinition.FieldModifiers;
import com.neaterbits.compiler.common.ast.typedefinition.FieldName;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceModifierHolder;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceModifiers;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceName;

final class ClassFileTyped extends ClassFile implements ClassBytecodeTyped {

	@Override
	public InvocableType<?> getType() {
		
		final Context context = null;

		final InvocableType<?> type;
		
		if ((getAccessFlags() & AccessFlags.ACC_INTERFACE) != 0) {
			final List<InterfaceModifierHolder> modifierList = new ArrayList<>();
			
			final InterfaceModifiers modifiers = new InterfaceModifiers(context, modifierList);

			final CompleteName completeName = getCompleteName(getThisClass(), InterfaceName::new);

			final List<TypeReference> extendsInterfaces = getInterfaces(context);
			
			final InterfaceDefinition interfaceDefinition = new InterfaceDefinition(
					context,
					modifiers,
					(InterfaceName)completeName.getName(),
					extendsInterfaces,
					getMembers(context));
			
			type = new InterfaceType(
					completeName.getNamespace(),
					completeName.getOuterTypes() != null ? completeName.getOuterTypes() : null,
					interfaceDefinition);
		}
		else if ((getAccessFlags() & AccessFlags.ACC_ENUM) != 0) {
			throw new UnsupportedOperationException();
		}
		else {
		
			final List<ClassModifierHolder> modifiersList = new ArrayList<>();
			
			final ClassModifiers modifiers = new ClassModifiers(context, modifiersList);
			
			final CompleteName completeName = getCompleteName(getThisClass(), ClassName::new);
			
			final ResolveLaterTypeReference superclassTypeReference = getTypeReference(context, getSuperClassIndex(), ClassName::new);
			
			final List<TypeReference> extendsClasses = Arrays.asList(superclassTypeReference);
			
			final ClassDefinition classDefinition = new ClassDefinition(
					context,
					modifiers,
					(ClassName)completeName.getName(),
					extendsClasses,
					new ArrayList<TypeReference>(),
					getMembers(context));
			
			type = new ClassType(
					completeName.getNamespace(),
					completeName.getOuterTypes() != null ? completeName.getOuterTypes() : null,
					classDefinition);
		}
			
		return type;
	}

	private ClassDataFieldMember getField(Context context, Field field) {
		
		final List<FieldModifierHolder> modifierList = new ArrayList<>();
		final FieldModifiers modifiers = new FieldModifiers(context, modifierList);
		
		final String descriptor = getUTF8(field.getDescriptorIndex());
		
		return new ClassDataFieldMember(
				context,
				modifiers,
				new EncodedTypeReference(context, descriptor),
				new FieldName(getUTF8(field.getNameIndex())),
				null);
	}

	private ResolveLaterTypeReference getTypeReference(Context context, int typeName, Function<String, BaseTypeName> createTypeName) {
		final CompleteName className = getCompleteName(getSuperClassIndex(), createTypeName);
		
		final ResolveLaterTypeReference typeReference = new ResolveLaterTypeReference(context, className.toScopedName());

		return typeReference;
	}
	
	private List<ComplexMemberDefinition> getMembers(Context context) {
		final List<ComplexMemberDefinition> members = new ArrayList<>(getFields().length + getMethods().length);
		
		for (Field field : getFields()) {
			members.add(getField(context, field));
		}
		
		for (Method method : getMethods()) {
			members.add(getMethod(context, method));
		}
		
		return members;
	}
	

	private ClassMethodMember getMethod(Context context, Method method) {
		
		final List<ClassMethodModifierHolder> modifierList = new ArrayList<>();
		final ClassMethodModifiers modifiers = new ClassMethodModifiers(context, modifierList);
		

		final List<TypeReference> paramTypes = new ArrayList<>();

		final String returnTypeDescriptor = getMethodDescriptorTypes(
				method.getDescriptorIndex(),
				encodedType -> paramTypes.add(new EncodedTypeReference(context, encodedType)));
		
		final EncodedTypeReference returnType = new EncodedTypeReference(context, returnTypeDescriptor);
		
		final List<Parameter> params = new ArrayList<>(paramTypes.size());
		
		for (int i = 0; i < paramTypes.size(); ++ i) {
			params.add(new Parameter(context, paramTypes.get(i), new ParameterName("param" + i), false));
		}
		
		final ClassMethod classMethod = new ClassMethod(
				context,
				returnType,
				getUTF8(method.getNameIndex()),
				params,
				null);
		
		return new ClassMethodMember(context, modifiers, classMethod);
	}
	
	private List<TypeReference> getInterfaces(Context context) {
		
		final List<TypeReference> interfaces = new ArrayList<>(this.getInterfaces().length);
	
		for (int interfaceIndex : this.getInterfaces()) {
			
			final TypeReference typeReference = new ResolveLaterTypeReference(context, getCompleteName(interfaceIndex, InterfaceName::new).toScopedName());
			
			interfaces.add(typeReference);
		}
		
		return interfaces;
	}
}
