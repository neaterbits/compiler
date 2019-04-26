package com.neaterbits.compiler.ast.block;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.ast.statement.Statement;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

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
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.BLOCK;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(statements, recurseMode, iterator);
	}
}
