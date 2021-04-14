package dev.nimbler.compiler.ast.objects.statement;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.block.Block;
import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class WhileStatement extends LoopStatement {

	private final ASTSingle<Expression> condition;
	
	public WhileStatement(Context context, Expression condition, Block block) {
		super(context, block);

		Objects.requireNonNull(condition);
		
		this.condition = makeSingle(condition);
	}

	public Expression getCondition() {
		return condition.get();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.WHILE_STATEMENT;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onWhile(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(condition, recurseMode, iterator);
		
		super.doRecurse(recurseMode, iterator);
	}
}
