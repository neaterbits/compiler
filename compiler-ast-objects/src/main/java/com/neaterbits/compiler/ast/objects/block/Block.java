package com.neaterbits.compiler.ast.objects.block;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.ast.objects.statement.Statement;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.util.parse.context.Context;

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
