package dev.nimbler.compiler.ast.objects.statement;

import java.util.List;
import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.ast.objects.block.Block;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.types.ParseTreeElement;

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
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.SWITCH_CASE_GROUP;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
		doIterate(labels, recurseMode, iterator);
		doIterate(block, recurseMode, iterator);
	}
}
