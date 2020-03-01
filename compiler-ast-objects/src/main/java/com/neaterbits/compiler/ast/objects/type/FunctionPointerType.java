package com.neaterbits.compiler.ast.objects.type;

import java.util.List;
import java.util.Objects;

public final class FunctionPointerType extends BaseType {

	private final BaseType returnType;
	private final List<FunctionPointerParameter> parameters;

	public FunctionPointerType(BaseType returnType, List<FunctionPointerParameter> parameters) {
		super(true);
		
		Objects.requireNonNull(returnType);
		Objects.requireNonNull(parameters);
		
		this.returnType = returnType;
		this.parameters = parameters;
	}

	public BaseType getReturnType() {
		return returnType;
	}

	public List<FunctionPointerParameter> getParameters() {
		return parameters;
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onFunctionPointer(this, param);
	}
}
