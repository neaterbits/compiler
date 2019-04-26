package com.neaterbits.compiler.ast.block;


import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class ClassMethod extends CallableCode<MethodName> {

	public ClassMethod(Context context, TypeReference returnType, String name, Context nameContext, List<Parameter> parameters, Block block) {
		super(context, returnType, new MethodName(nameContext, name), parameters, block);

		Objects.requireNonNull(returnType);
	}
	
	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CLASS_METHOD;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onClassMethod(this, param);
	}
}
