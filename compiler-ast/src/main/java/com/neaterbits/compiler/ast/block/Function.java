package com.neaterbits.compiler.ast.block;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

public final class Function extends CallableCode<FunctionName> {

	private final FunctionQualifiers qualifiers;
	
	public Function(Context context, FunctionQualifiers qualifiers, TypeReference returnType, FunctionName name, List<Parameter> parameters, Block block) {
		super(context, returnType, name, parameters, block);
		
		Objects.requireNonNull(returnType);
		Objects.requireNonNull(qualifiers);
		
		this.qualifiers = qualifiers;
	}
	
	public FunctionQualifiers getQualifiers() {
		return qualifiers;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onFunction(this, param);
	}
}
