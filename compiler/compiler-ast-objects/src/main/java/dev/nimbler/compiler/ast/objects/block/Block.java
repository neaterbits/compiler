package dev.nimbler.compiler.ast.objects.block;

import java.util.List;
import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.ast.objects.statement.Statement;
import dev.nimbler.compiler.types.ParseTreeElement;

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
