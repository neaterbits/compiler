package com.neaterbits.compiler.ast.objects.statement;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.Keyword;
import com.neaterbits.compiler.ast.objects.block.Block;
import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.util.parse.context.Context;

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
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.IF_ELSE_IF_ELSE_STATEMENT;
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
