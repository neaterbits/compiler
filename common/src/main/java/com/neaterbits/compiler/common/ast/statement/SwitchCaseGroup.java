package com.neaterbits.compiler.common.ast.statement;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

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
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		
		doIterate(labels, recurseMode, visitor);
		doIterate(block, recurseMode, visitor);
	}
}
