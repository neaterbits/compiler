package dev.nimbler.compiler.ast.objects;

import dev.nimbler.compiler.ast.objects.block.ClassMethod;
import dev.nimbler.compiler.ast.objects.block.Constructor;
import dev.nimbler.compiler.ast.objects.block.Function;
import dev.nimbler.compiler.ast.objects.block.StaticInitializer;
import dev.nimbler.compiler.ast.objects.statement.Statement;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDataFieldMember;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassMethodMember;
import dev.nimbler.compiler.ast.objects.typedefinition.ConstructorMember;
import dev.nimbler.compiler.ast.objects.typedefinition.EnumConstantDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.EnumDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.InnerClassMember;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceMethod;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceMethodMember;
import dev.nimbler.compiler.ast.objects.typedefinition.StructDataFieldMember;
import dev.nimbler.compiler.ast.objects.typedefinition.StructDefinition;

public interface CompilationCodeVisitor<T, R> {

	R onFunction(Function function, T param);

	R onStructDefinition(StructDefinition structDefinition, T param);

	R onStructDataFieldMember(StructDataFieldMember field, T param);

	R onClassDefinition(ClassDefinition classDefinition, T param);

	R onEnumDefinition(EnumDefinition enumDefinition, T param);
	
	R onEnumConstantDefinition(EnumConstantDefinition enumConstantDefinition, T param);
	
	R onConstructor(Constructor constructor, T param);
	
	R onConstructorMember(ConstructorMember constructor, T param);
	
	R onClassMethod(ClassMethod method, T param);

	R onClassMethodMember(ClassMethodMember method, T param);

	R onClassDataFieldMember(ClassDataFieldMember field, T param);
	
	R onStaticInitializer(StaticInitializer initializer, T param);

	R onInterfaceDefinition(InterfaceDefinition interfaceDefinition, T param);
	
	R onInterfaceMethod(InterfaceMethod method, T param);

	R onInterfaceMethodMember(InterfaceMethodMember method, T param);

	R onInnerClassMember(InnerClassMember field, T param);

	R onNamespace(Namespace namespace, T param);

	R onStatement(Statement statement, T param);
}
