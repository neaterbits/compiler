package com.neaterbits.compiler.common.ast.statement;

import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.expression.Resource;
import com.neaterbits.compiler.common.ast.expression.ResourceList;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public final class TryWithResourcesStatement extends BaseTryCatchFinallyStatement {

	private final ASTSingle<ResourceList> resources;
	
	public TryWithResourcesStatement(Context context, List<Resource> resources, Block tryBlock, List<CatchBlock> catchBlocks, Block finallyBlock) {
		super(context, tryBlock, catchBlocks, finallyBlock);
		
		this.resources = makeSingle(new ResourceList(context, resources));
	}

	public ResourceList getResources() {
		return resources.get();
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onTryWithResourcesStatement(this, param);
	}
}
