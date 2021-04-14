package dev.nimbler.compiler.main;

import dev.nimbler.compiler.ast.objects.block.ConstructorInvocationStatement;
import dev.nimbler.compiler.ast.objects.statement.IteratorForStatement;
import dev.nimbler.compiler.ast.objects.statement.Statement;
import dev.nimbler.compiler.ast.objects.statement.ThrowStatement;
import dev.nimbler.compiler.ast.objects.statement.TryCatchFinallyStatement;
import dev.nimbler.compiler.ast.objects.statement.TryWithResourcesStatement;
import dev.nimbler.compiler.convert.ootofunction.BaseStatementConverter;

final class JavaToCStatementConverter<T extends MappingJavaToCConverterState<T>> extends BaseStatementConverter<T> {

	@Override
	public Statement onIteratorFor(IteratorForStatement statement, T param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement onTryCatchFinallyStatement(TryCatchFinallyStatement statement, T param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement onTryWithResourcesStatement(TryWithResourcesStatement statement, T param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement onThrowStatement(ThrowStatement statement, T param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement onConstructorInvocation(ConstructorInvocationStatement statement, T param) {
		// TODO Auto-generated method stub
		return null;
	}
}
