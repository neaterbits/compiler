package com.neaterbits.compiler.ast;

import com.neaterbits.compiler.ast.block.ClassMethod;
import com.neaterbits.compiler.ast.block.Function;
import com.neaterbits.compiler.ast.typedefinition.ClassDataFieldMember;
import com.neaterbits.compiler.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.ast.typedefinition.ClassMethodMember;
import com.neaterbits.compiler.ast.typedefinition.StructDataFieldMember;
import com.neaterbits.compiler.ast.typedefinition.StructDefinition;

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
	public R onClassMethod(ClassMethod method, T param) {
		throw new UnsupportedOperationException();
	}

	@Override
	public R onClassMethodMember(ClassMethodMember method, T param) {
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
