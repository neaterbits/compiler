package com.neaterbits.compiler.common.ast.statement;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.variables.VarName;

public final class CatchBlock extends BaseASTElement {

	private final List<TypeReference> exceptionTypes;
	private final VarName exceptionVarName;
	
	private final Block block;
	
	public CatchBlock(Context context, List<TypeReference> exceptionTypes, VarName exceptionVarName, Block block) {
		super(context);
		
		Objects.requireNonNull(exceptionTypes);
		
		if (exceptionTypes.isEmpty()) {
			throw new IllegalArgumentException("No exception types");
		}
		
		Objects.requireNonNull(exceptionVarName);
		Objects.requireNonNull(block);

		this.exceptionTypes = exceptionTypes;
		this.exceptionVarName = exceptionVarName;
		this.block = block;
	}

	public List<TypeReference> getExceptionTypes() {
		return exceptionTypes;
	}

	public VarName getExceptionVarName() {
		return exceptionVarName;
	}

	public Block getBlock() {
		return block;
	}
}
