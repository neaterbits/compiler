package com.neaterbits.compiler.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.expression.literal.Primary;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.type.FunctionPointerType;
import com.neaterbits.compiler.ast.typereference.FunctionPointerTypeReference;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

public final class FunctionPointerInvocationExpression extends Expression {

	private final FunctionPointerType type;
	private final ASTSingle<Primary> functionPointer;
	private final ASTSingle<ParameterList> parameterList;
	
	public FunctionPointerInvocationExpression(Context context, FunctionPointerType type, Primary functionPointer, ParameterList parameterList) {
		super(context);
	
		Objects.requireNonNull(type);
		Objects.requireNonNull(functionPointer);
		Objects.requireNonNull(parameterList);
		
		this.type = type;
		this.functionPointer = makeSingle(functionPointer);
		this.parameterList = makeSingle(parameterList);
	}

	
	public Primary getFunctionPointer() {
		return functionPointer.get();
	}
	
	public ParameterList getParameters() {
		return parameterList.get();
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onFunctionPointerInvocation(this, param);
	}

	@Override
	public TypeReference getType() {
		return new FunctionPointerTypeReference(getContext(), type);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(functionPointer, recurseMode, iterator);
		doIterate(parameterList, recurseMode, iterator);
	}
}
