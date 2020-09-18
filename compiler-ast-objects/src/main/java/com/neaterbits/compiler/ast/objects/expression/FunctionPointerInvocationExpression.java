package com.neaterbits.compiler.ast.objects.expression;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.expression.literal.Primary;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.type.FunctionPointerType;
import com.neaterbits.compiler.ast.objects.typereference.FunctionPointerTypeReference;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.types.ParseTreeElement;
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
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.FUNCTION_POINTER_INVOCATION_EXPRESSION;
	}


	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(functionPointer, recurseMode, iterator);
		doIterate(parameterList, recurseMode, iterator);
	}
}
