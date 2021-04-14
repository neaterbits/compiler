package dev.nimbler.compiler.bytecode.java.ast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.block.ClassMethod;
import dev.nimbler.compiler.ast.objects.block.Parameter;
import dev.nimbler.compiler.ast.objects.block.ParameterName;
import dev.nimbler.compiler.ast.objects.type.CompleteName;
import dev.nimbler.compiler.ast.objects.type.complex.ClassType;
import dev.nimbler.compiler.ast.objects.type.complex.InterfaceType;
import dev.nimbler.compiler.ast.objects.type.complex.InvocableType;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDataFieldMember;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDeclarationName;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassMethodMember;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassMethodModifierHolder;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassMethodModifiers;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassModifierHolder;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassModifiers;
import dev.nimbler.compiler.ast.objects.typedefinition.ComplexMemberDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.FieldModifierHolder;
import dev.nimbler.compiler.ast.objects.typedefinition.FieldModifiers;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceDeclarationName;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceModifierHolder;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceModifiers;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceName;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.typereference.UnresolvedTypeReference;
import dev.nimbler.compiler.ast.objects.variables.InitializerVariableDeclarationElement;
import dev.nimbler.compiler.ast.objects.variables.VarNameDeclaration;
import dev.nimbler.compiler.bytecode.ast.BytecodeEncodedTypeReference;
import dev.nimbler.compiler.bytecode.ast.ClassBytecodeTyped;
import dev.nimbler.compiler.types.ReferenceType;
import dev.nimbler.compiler.util.name.BaseTypeName;
import dev.nimbler.compiler.util.name.ClassName;
import dev.nimbler.compiler.util.name.DefinitionName;
import dev.nimbler.compiler.util.name.NamespaceReference;
import dev.nimbler.language.bytecode.java.AccessFlags;
import dev.nimbler.language.bytecode.java.ClassFile;
import dev.nimbler.language.bytecode.java.Field;
import dev.nimbler.language.bytecode.java.Method;
import dev.nimbler.language.common.types.TypeName;

final class ClassFileTyped extends ClassFile implements ClassBytecodeTyped {

	@Override
	public InvocableType<?, ?, ?> getType() {
		
		final Context context = null;

		final InvocableType<?, ?, ?> type;
		
		if ((getAccessFlags() & AccessFlags.ACC_INTERFACE) != 0) {
			final List<InterfaceModifierHolder> modifierList = new ArrayList<>();
			
			final InterfaceModifiers modifiers = new InterfaceModifiers(null, modifierList);

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
			
			final ClassModifiers modifiers = new ClassModifiers(null, modifiersList);
			
			final CompleteName completeName = getCompleteName(getThisClass(), ClassName::new);
			
			final UnresolvedTypeReference superclassTypeReference = getTypeReference(context, getSuperClassIndex(), ClassName::new);
			
			final List<TypeReference> extendsClasses = Arrays.asList(superclassTypeReference);
			
			final ClassDefinition classDefinition = new ClassDefinition(
					context,
					modifiers,
					null,
					new ClassDeclarationName(context, (ClassName)completeName.getName()),
					null,
					null,
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
		final FieldModifiers modifiers = new FieldModifiers(null, modifierList);
		
		final String descriptor = getUTF8(field.getDescriptorIndex());
		
		final String name = getUTF8(field.getNameIndex());
		
		final InitializerVariableDeclarationElement initializer = new InitializerVariableDeclarationElement(
		        null,
		        new VarNameDeclaration(null, name),
		        0,
		        null);
		
		return new ClassDataFieldMember(
				context,
				modifiers,
				new BytecodeEncodedTypeReference(context, descriptor),
				Arrays.asList(initializer));
	}

	private UnresolvedTypeReference getTypeReference(Context context, int typeName, Function<String, BaseTypeName> createTypeName) {
		final CompleteName className = getCompleteName(getSuperClassIndex(), createTypeName);
		
		final UnresolvedTypeReference typeReference = new UnresolvedTypeReference(
		        context,
		        className.toScopedName(),
		        null,
		        ReferenceType.NAME);

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
		final ClassMethodModifiers modifiers = new ClassMethodModifiers(null, modifierList);
		

		final List<TypeReference> paramTypes = new ArrayList<>();

		final String returnTypeDescriptor = getMethodDescriptorTypes(
				method.getDescriptorIndex(),
				encodedType -> paramTypes.add(new BytecodeEncodedTypeReference(context, encodedType)));
		
		final BytecodeEncodedTypeReference returnType = new BytecodeEncodedTypeReference(context, returnTypeDescriptor);
		
		final List<Parameter> params = new ArrayList<>(paramTypes.size());
		
		for (int i = 0; i < paramTypes.size(); ++ i) {
			params.add(new Parameter(context, null, paramTypes.get(i), new ParameterName(null, "param" + i), false));
		}
		
		final ClassMethod classMethod = new ClassMethod(
				context,
				null,
				returnType,
				getUTF8(method.getNameIndex()),
				null,
				params,
				null,
				null);
		
		return new ClassMethodMember(context, modifiers, classMethod);
	}
	
	private List<TypeReference> getInterfaces(Context context) {
		
		final List<TypeReference> interfaces = new ArrayList<>(this.getInterfaces().length);
	
		for (int interfaceIndex : this.getInterfaces()) {
			
			final TypeReference typeReference = new UnresolvedTypeReference(
					context,
					getCompleteName(interfaceIndex, InterfaceName::new).toScopedName(),
					null,
					ReferenceType.NAME);
			
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
