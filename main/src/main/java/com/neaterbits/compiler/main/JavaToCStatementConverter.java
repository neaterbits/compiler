package com.neaterbits.compiler.main;

import com.neaterbits.compiler.common.ast.block.ConstructorInvocationStatement;
import com.neaterbits.compiler.common.ast.statement.IteratorForStatement;
import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.ast.statement.ThrowStatement;
import com.neaterbits.compiler.common.ast.statement.TryCatchFinallyStatement;
import com.neaterbits.compiler.common.ast.statement.TryWithResourcesStatement;
import com.neaterbits.compiler.common.convert.ootofunction.BaseStatementConverter;

final class JavaToCStatementConverter extends BaseStatementConverter<JavaToCConverterState> {

	@Override
	public Statement onIteratorFor(IteratorForStatement statement, JavaToCConverterState param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement onTryCatchFinallyStatement(TryCatchFinallyStatement statement, JavaToCConverterState param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement onTryWithResourcesStatement(TryWithResourcesStatement statement, JavaToCConverterState param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement onThrowStatement(ThrowStatement statement, JavaToCConverterState param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Statement onConstructorInvocation(ConstructorInvocationStatement statement, JavaToCConverterState param) {
		// TODO Auto-generated method stub
		return null;
	}
}
