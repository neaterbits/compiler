package dev.nimbler.compiler.ast.objects.statement;

import java.util.List;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.block.Block;
import dev.nimbler.compiler.ast.objects.expression.Resource;
import dev.nimbler.compiler.ast.objects.expression.ResourceList;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.types.ParseTreeElement;

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
