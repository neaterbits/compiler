package com.neaterbits.compiler.common.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.expression.literal.Primary;
import com.neaterbits.compiler.common.ast.list.ASTSingle;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.type.FunctionPointerType;

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
	public BaseType getType() {
		return type;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(functionPointer, recurseMode, iterator);
		doIterate(parameterList, recurseMode, iterator);
	}
}
