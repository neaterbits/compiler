package com.neaterbits.compiler.common.ast.block;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.ast.statement.Statement;

public final class Block extends BaseASTElement {
	
	private final ASTList<Statement> statements;
	
	public Block(Context context, List<Statement> statements) {
		super(context);
		
		Objects.requireNonNull(statements);
		
		this.statements = makeList(statements);
	}

	public final ASTList<Statement> getStatements() {
		return statements;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(statements, recurseMode, iterator);
	}
}
