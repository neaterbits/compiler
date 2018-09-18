package com.neaterbits.compiler.common.convert.ootofunction;

import java.util.ArrayList;
import java.util.List;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.block.Parameter;
import com.neaterbits.compiler.common.ast.list.ASTList;

public abstract class IterativeConverter {

	protected final Block convertBlock(Block block) {
		throw new UnsupportedOperationException("TODO");
	}
	
	protected final List<Parameter> convertParameters(ASTList<Parameter> parameters, java.util.function.Function<TypeReference, TypeReference> convertType) {
		
		final List<Parameter> result = new ArrayList<>(parameters.size());
		
		for (Parameter parameter : parameters) {
			result.add(new Parameter(
					parameter.getContext(),
					convertType.apply(parameter.getType()),
					parameter.getName()));
		}

		return result;
	}
	
}
