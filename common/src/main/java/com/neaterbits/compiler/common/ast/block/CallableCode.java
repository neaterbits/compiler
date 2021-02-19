package com.neaterbits.compiler.common.ast.block;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

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
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {

		super.doRecurse(recurseMode, visitor);
		
		doIterate(block, recurseMode, visitor);
	}
}
