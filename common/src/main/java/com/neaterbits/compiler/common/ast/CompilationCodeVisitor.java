package com.neaterbits.compiler.common.ast;

import com.neaterbits.compiler.common.ast.block.Constructor;
import com.neaterbits.compiler.common.ast.block.Function;
import com.neaterbits.compiler.common.ast.block.StaticInitializer;
import com.neaterbits.compiler.common.ast.block.ClassMethod;
import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.ConstructorMember;
import com.neaterbits.compiler.common.ast.typedefinition.EnumConstantDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.EnumDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.InnerClassMember;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceMethod;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceMethodMember;
import com.neaterbits.compiler.common.ast.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.common.ast.typedefinition.StructDataFieldMember;
import com.neaterbits.compiler.common.ast.typedefinition.StructDefinition;

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
