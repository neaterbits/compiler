package com.neaterbits.compiler.ast.objects.statement;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.block.Block;
import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.ast.objects.variables.VarName;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.Context;

public final class CatchBlock extends BaseASTElement {

	private final ASTList<TypeReference> exceptionTypes;
	private final VarName exceptionVarName;
	
	private final ASTSingle<Block> block;
	
	public CatchBlock(Context context, List<TypeReference> exceptionTypes, VarName exceptionVarName, Block block) {
		super(context);
		
		Objects.requireNonNull(exceptionTypes);
		
		if (exceptionTypes.isEmpty()) {
			throw new IllegalArgumentException("No exception types");
		}
		
		Objects.requireNonNull(exceptionVarName);
		Objects.requireNonNull(block);

		this.exceptionTypes = makeList(exceptionTypes);
		this.exceptionVarName = exceptionVarName;
		this.block = makeSingle(block);
	}

	public ASTList<TypeReference> getExceptionTypes() {
		return exceptionTypes;
	}

	public VarName getExceptionVarName() {
		return exceptionVarName;
	}

	public Block getBlock() {
		return block.get();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CATCH_BLOCK;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(exceptionTypes, recurseMode, iterator);
		doIterate(block, recurseMode, iterator);
	}
}
