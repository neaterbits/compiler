package com.neaterbits.compiler.ast.statement;

import java.util.List;

import com.neaterbits.compiler.ast.block.Block;
import com.neaterbits.compiler.ast.expression.Resource;
import com.neaterbits.compiler.ast.expression.ResourceList;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

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
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.TRY_WITH_RESOURCES_STATEMENT;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onTryWithResourcesStatement(this, param);
	}
}
