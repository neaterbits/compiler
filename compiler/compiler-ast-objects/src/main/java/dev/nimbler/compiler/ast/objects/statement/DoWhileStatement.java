package dev.nimbler.compiler.ast.objects.statement;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.block.Block;
import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class DoWhileStatement extends LoopStatement {

	private final ASTSingle<Expression> condition;
	
	public DoWhileStatement(Context context, Expression condition, Block block) {
		super(context, block);
		
		Objects.requireNonNull(condition);
		
		this.condition = makeSingle(condition);
	}

	public Expression getCondition() {
		return condition.get();
	}
	
	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.DO_WHILE_STATEMENT;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onDoWhile(this, param);
	}
}
