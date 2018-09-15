package com.neaterbits.compiler.common.ast.statement;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.block.Block;

public final class IfElseIfElseStatement extends ConditionStatement {

	private final List<ConditionBlock> conditions;
	private final Block elseBlock;
	
	public IfElseIfElseStatement(Context context, List<ConditionBlock> conditions, Block elseBlock) {
		super(context);
		
		Objects.requireNonNull(conditions);
		
		if (conditions.isEmpty()) {
			throw new IllegalArgumentException("conditions is empty");
		}
		
		this.conditions = Collections.unmodifiableList(conditions);
		this.elseBlock = elseBlock;
	}

	public List<ConditionBlock> getConditions() {
		return conditions;
	}

	public Block getElseBlock() {
		return elseBlock;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onIf(this, param);
	}
}
