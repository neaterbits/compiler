package com.neaterbits.compiler.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.Keyword;
import com.neaterbits.compiler.ast.block.Block;
import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.util.Context;

public final class ConditionBlock extends BaseASTElement {

	private final ASTSingle<Keyword> elseKeyword;
	private final ASTSingle<Keyword> ifKeyword;
	private final ASTSingle<Expression> condition;
	private final ASTSingle<Block> block;
	
	public ConditionBlock(Context context, Keyword elseKeyword, Keyword ifKeyword, Expression condition, Block block) {
		super(context);
		
		if (elseKeyword != null && elseKeyword.getContext().getStartOffset() != context.getStartOffset()) {
			throw new IllegalArgumentException("offset mismatch " + elseKeyword.getContext().getStartOffset() + "/" + context.getStartOffset());
		}
		
		Objects.requireNonNull(condition);
		Objects.requireNonNull(block);
		
		this.elseKeyword = elseKeyword != null ? makeSingle(elseKeyword) : null;
		this.ifKeyword = ifKeyword != null ? makeSingle(ifKeyword) : null;
		
		this.condition = makeSingle(condition);
		this.block = makeSingle(block);
	}
	
	public Keyword getElseKeyword() {
		return elseKeyword != null ? elseKeyword.get() : null;
	}

	public Keyword getIfKeyword() {
		return ifKeyword != null ? ifKeyword.get() : null;
	}

	public Expression getCondition() {
		return condition.get();
	}

	public Block getBlock() {
		return block.get();
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
		if (elseKeyword != null) {
			doIterate(elseKeyword, recurseMode, iterator);
		}

		if (ifKeyword != null) {
			doIterate(ifKeyword, recurseMode, iterator);
		}

		doIterate(condition, recurseMode, iterator);
		doIterate(block, recurseMode, iterator);
	}
}
