package com.neaterbits.compiler.common.ast;

import com.neaterbits.compiler.common.ast.block.Function;
import com.neaterbits.compiler.common.ast.block.Method;
import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.InnerClassMember;
import com.neaterbits.compiler.common.ast.typedefinition.MethodMember;
import com.neaterbits.compiler.common.ast.typedefinition.StructDataFieldMember;
import com.neaterbits.compiler.common.ast.typedefinition.StructDefinition;

public interface CompilationCodeVisitor<T, R> {

	R onFunction(Function function, T param);

	R onStructDefinition(StructDefinition structDefinition, T param);

	R onStructDataFieldMember(StructDataFieldMember field, T param);

	R onClassDefinition(ClassDefinition classDefinition, T param);
	
	R onMethod(Method method, T param);

	R onMethodMember(MethodMember method, T param);

	R onClassDataFieldMember(ClassDataFieldMember field, T param);

	R onInnerClassMember(InnerClassMember field, T param);

	R onNamespace(Namespace namespace, T param);

	R onStatement(Statement statement, T param);
}
