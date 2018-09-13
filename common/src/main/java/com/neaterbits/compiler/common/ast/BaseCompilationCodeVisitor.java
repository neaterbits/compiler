package com.neaterbits.compiler.common.ast;

import com.neaterbits.compiler.common.ast.block.Function;
import com.neaterbits.compiler.common.ast.block.Method;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.MethodMember;
import com.neaterbits.compiler.common.ast.typedefinition.StructDataFieldMember;
import com.neaterbits.compiler.common.ast.typedefinition.StructDefinition;

public abstract class BaseCompilationCodeVisitor<T, R> implements CompilationCodeVisitor<T, R> {

	@Override
	public R onFunction(Function function, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public R onStructDefinition(StructDefinition structDefinition, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public R onStructDataFieldMember(StructDataFieldMember field, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public R onClassDefinition(ClassDefinition classDefinition, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public R onMethod(Method method, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public R onMethodMember(MethodMember method, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public R onClassDataFieldMember(ClassDataFieldMember field, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public R onNamespace(Namespace namespace, T param) {
		throw new UnsupportedOperationException();
	}
}
