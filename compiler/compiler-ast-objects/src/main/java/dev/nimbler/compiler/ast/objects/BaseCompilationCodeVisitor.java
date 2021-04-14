package dev.nimbler.compiler.ast.objects;

import dev.nimbler.compiler.ast.objects.block.ClassMethod;
import dev.nimbler.compiler.ast.objects.block.Function;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDataFieldMember;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassMethodMember;
import dev.nimbler.compiler.ast.objects.typedefinition.StructDataFieldMember;
import dev.nimbler.compiler.ast.objects.typedefinition.StructDefinition;

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
