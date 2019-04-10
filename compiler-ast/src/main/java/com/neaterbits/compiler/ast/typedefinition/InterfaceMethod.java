package com.neaterbits.compiler.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.block.Callable;
import com.neaterbits.compiler.ast.block.Parameter;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

public final class InterfaceMethod extends Callable<InterfaceMethodName> {

	public InterfaceMethod(Context context, TypeReference returnType, String name, Context nameContext,
			List<Parameter> parameters) {
		super(context, returnType, new InterfaceMethodName(nameContext, name), parameters);
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onInterfaceMethod(this, param);
	}
}
