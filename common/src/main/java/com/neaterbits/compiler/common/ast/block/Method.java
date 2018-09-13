package com.neaterbits.compiler.common.ast.block;


import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;

public final class Method extends Callable<MethodName> {

	public Method(Context context, TypeReference returnType, String name, List<Parameter> parameters, Block block) {
		super(context, returnType, new MethodName(name), parameters, block);
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onMethod(this, param);
	}
}
