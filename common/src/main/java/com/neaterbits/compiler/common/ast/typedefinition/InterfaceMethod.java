package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.common.ast.block.Callable;
import com.neaterbits.compiler.common.ast.block.Parameter;

public final class InterfaceMethod extends Callable<InterfaceMethodName> {

	public InterfaceMethod(Context context, TypeReference returnType, String name,
			List<Parameter> parameters) {
		super(context, returnType, new InterfaceMethodName(name), parameters);
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onInterfaceMethod(this, param);
	}
}
