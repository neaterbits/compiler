package com.neaterbits.compiler.main;

import com.neaterbits.compiler.common.ast.block.ConstructorInvocationStatement;
import com.neaterbits.compiler.common.ast.statement.IteratorForStatement;
import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.ast.statement.ThrowStatement;
import com.neaterbits.compiler.common.ast.statement.TryCatchFinallyStatement;
import com.neaterbits.compiler.common.ast.statement.TryWithResourcesStatement;
import com.neaterbits.compiler.common.convert.ootofunction.BaseStatementConverter;

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
