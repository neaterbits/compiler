package com.neaterbits.compiler.ast.objects.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.block.Block;
import com.neaterbits.compiler.ast.objects.expression.Expression;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.Context;

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
