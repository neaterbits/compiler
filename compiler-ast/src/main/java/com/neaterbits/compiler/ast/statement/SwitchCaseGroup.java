package com.neaterbits.compiler.ast.statement;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.block.Block;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.util.Context;

public final class SwitchCaseGroup extends BaseASTElement {

	private final ASTList<SwitchCaseLabel> labels;
	private final ASTSingle<Block> block;
	
	public SwitchCaseGroup(Context context, List<SwitchCaseLabel> labels, Block block) {
		super(context);

		Objects.requireNonNull(labels);
		Objects.requireNonNull(block);
		
		this.labels = makeList(labels);
		this.block = makeSingle(block);
	}

	public ASTList<SwitchCaseLabel> getLabels() {
		return labels;
	}

	public Block getBlock() {
		return block.get();
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
		doIterate(labels, recurseMode, iterator);
		doIterate(block, recurseMode, iterator);
	}
}