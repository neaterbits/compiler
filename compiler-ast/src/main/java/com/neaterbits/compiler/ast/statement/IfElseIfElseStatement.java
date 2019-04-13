package com.neaterbits.compiler.ast.statement;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.Keyword;
import com.neaterbits.compiler.ast.block.Block;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.util.Context;

public final class IfElseIfElseStatement extends ConditionStatement {

	private final ASTList<ConditionBlock> conditions;
	
	private final ASTSingle<Keyword> elseKeyword;
	private final ASTSingle<Block> elseBlock;
	
	public IfElseIfElseStatement(Context context, List<ConditionBlock> conditions, Keyword elseKeyword, Block elseBlock) {
		super(context);
		
		Objects.requireNonNull(conditions);
		
		if (conditions.isEmpty()) {
			throw new IllegalArgumentException("conditions is empty");
		}
		
		this.conditions = makeList(conditions);
		
		this.elseKeyword = elseKeyword != null ? makeSingle(elseKeyword) : null;
		this.elseBlock = elseBlock != null ? makeSingle(elseBlock) : null;
	}

	public ASTList<ConditionBlock> getConditions() {
		return conditions;
	}

	public Keyword getElseKeyword() {
		return elseKeyword != null ? elseKeyword.get() : null;
	}

	public Block getElseBlock() {
		return elseBlock != null ? elseBlock.get() : null;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onIf(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(conditions, recurseMode, iterator);
		
		if (elseBlock != null) {
			doIterate(elseKeyword, recurseMode, iterator);
			doIterate(elseBlock, recurseMode, iterator);
		}
	}
}
