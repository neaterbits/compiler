package com.neaterbits.compiler.ast.block;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

public abstract class CallableCode<NAME extends CallableName> extends Callable<NAME>  {

	private final ASTSingle<Block> block;

	CallableCode(Context context, TypeReference returnType, NAME name, List<Parameter> parameters, Block block) {
		super(context, returnType, name, parameters);
		
		Objects.requireNonNull(block);

		this.block = makeSingle(block);
	}
	
	public final Block getBlock() {
		return block.get();
	}


	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		super.doRecurse(recurseMode, iterator);
		
		doIterate(block, recurseMode, iterator);
	}
}
