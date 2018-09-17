package com.neaterbits.compiler.common.ast.statement;

import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.expression.Resource;
import com.neaterbits.compiler.common.ast.expression.ResourceList;

public final class TryWithResourcesStatement extends BaseTryCatchFinallyStatement {

	private final ResourceList resources;
	
	public TryWithResourcesStatement(Context context, List<Resource> resources, Block tryBlock, List<CatchBlock> catchBlocks, Block finallyBlock) {
		super(context, tryBlock, catchBlocks, finallyBlock);
		
		this.resources = new ResourceList(context, resources);
	}

	public ResourceList getResources() {
		return resources;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onTryWithResourcesStatement(this, param);
	}
}
