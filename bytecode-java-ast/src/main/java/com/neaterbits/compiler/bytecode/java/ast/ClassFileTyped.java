package com.neaterbits.compiler.bytecode.java.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import com.neaterbits.compiler.ast.FieldNameDeclaration;
import com.neaterbits.compiler.ast.NamespaceReference;
import com.neaterbits.compiler.ast.block.ClassMethod;
import com.neaterbits.compiler.ast.block.Parameter;
import com.neaterbits.compiler.ast.block.ParameterName;
import com.neaterbits.compiler.ast.type.BaseTypeName;
import com.neaterbits.compiler.ast.type.CompleteName;
import com.neaterbits.compiler.ast.type.complex.ClassType;
import com.neaterbits.compiler.ast.type.complex.InterfaceType;
import com.neaterbits.compiler.ast.type.complex.InvocableType;
import com.neaterbits.compiler.ast.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.ast.typedefinition.ClassDeclarationName;
import com.neaterbits.compiler.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodModifiers;
import com.neaterbits.compiler.ast.typedefinition.ClassModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.ClassModifiers;
import com.neaterbits.compiler.ast.typedefinition.ClassName;
import com.neaterbits.compiler.ast.typedefinition.ComplexMemberDefinition;
import com.neaterbits.compiler.ast.typedefinition.DefinitionName;
import com.neaterbits.compiler.ast.typedefinition.FieldModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.FieldModifiers;
import com.neaterbits.compiler.ast.typedefinition.InterfaceDeclarationName;
import com.neaterbits.compiler.ast.typedefinition.InterfaceDefinition;
import com.neaterbits.compiler.ast.typedefinition.InterfaceModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.InterfaceModifiers;
import com.neaterbits.compiler.ast.typedefinition.InterfaceName;
import com.neaterbits.compiler.ast.typereference.ResolveLaterTypeReference;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.bytecode.ast.ClassBytecodeTyped;
import com.neaterbits.compiler.bytecode.ast.EncodedTypeReference;
import com.neaterbits.compiler.java.bytecode.AccessFlags;
import com.neaterbits.compiler.java.bytecode.ClassFile;
import com.neaterbits.compiler.java.bytecode.Field;
import com.neaterbits.compiler.java.bytecode.Method;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.TypeName;

final class ClassFileTyped extends ClassFile implements ClassBytecodeTyped {

	@Override
	public InvocableType<?, ?, ?> getType() {
		
		final Context context = null;

		final InvocableType<?, ?, ?> type;
		
		if ((getAccessFlags() & AccessFlags.ACC_INTERFACE) != 0) {
			final List<InterfaceModifierHolder> modifierList = new ArrayList<>();
			
			final InterfaceModifiers modifiers = new InterfaceModifiers(modifierList);

			final CompleteName completeName = getCompleteName(getThisClass(), InterfaceName::new);

			final List<TypeReference> extendsInterfaces = getInterfaces(context);
			
			final InterfaceDefinition interfaceDefinition = new InterfaceDefinition(
					context,
					modifiers,
					null,
					new InterfaceDeclarationName(context, (InterfaceName)completeName.getName()),
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
			
			final ClassModifiers modifiers = new ClassModifiers(modifiersList);
			
			final CompleteName completeName = getCompleteName(getThisClass(), ClassName::new);
			
			final ResolveLaterTypeReference superclassTypeReference = getTypeReference(context, getSuperClassIndex(), ClassName::new);
			
			final List<TypeReference> extendsClasses = Arrays.asList(superclassTypeReference);
			
			final ClassDefinition classDefinition = new ClassDefinition(
					context,
					modifiers,
					null,
					new ClassDeclarationName(context, (ClassName)completeName.getName()),
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
		final FieldModifiers modifiers = new FieldModifiers(modifierList);
		
		final String descriptor = getUTF8(field.getDescriptorIndex());
		
		return new ClassDataFieldMember(
				context,
				modifiers,
				new EncodedTypeReference(context, descriptor),
				new FieldNameDeclaration(null, getUTF8(field.getNameIndex())),
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
		final ClassMethodModifiers modifiers = new ClassMethodModifiers(modifierList);
		

		final List<TypeReference> paramTypes = new ArrayList<>();

		final String returnTypeDescriptor = getMethodDescriptorTypes(
				method.getDescriptorIndex(),
				encodedType -> paramTypes.add(new EncodedTypeReference(context, encodedType)));
		
		final EncodedTypeReference returnType = new EncodedTypeReference(context, returnTypeDescriptor);
		
		final List<Parameter> params = new ArrayList<>(paramTypes.size());
		
		for (int i = 0; i < paramTypes.size(); ++ i) {
			params.add(new Parameter(context, paramTypes.get(i), new ParameterName(null, "param" + i), false));
		}
		
		final ClassMethod classMethod = new ClassMethod(
				context,
				returnType,
				getUTF8(method.getNameIndex()),
				null,
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

	final CompleteName getCompleteName(int classIndex, Function<String, BaseTypeName> createTypeName) {
		
		final TypeName typeName = getTypeName(classIndex);
		
		final List<DefinitionName> outerTypes;
		
		if (typeName.getOuterTypes() != null) {
			outerTypes = new ArrayList<>(typeName.getOuterTypes().length);
		
			for (String outerType : typeName.getOuterTypes()) {
				outerTypes.add(new ClassName(outerType));
			}
		}
		else {
			outerTypes = null;
		}
		
		return new CompleteName(
				new NamespaceReference(typeName.getNamespace()),
				outerTypes,
				createTypeName.apply(typeName.getName()));
	}
}
