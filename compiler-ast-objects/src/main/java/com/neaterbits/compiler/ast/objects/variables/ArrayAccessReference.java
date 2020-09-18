package com.neaterbits.compiler.ast.objects.variables;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.expression.ArrayAccessExpression;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.Context;

public final class ArrayAccessReference extends VariableReference {
	
	private final ArrayAccessExpression expression;
	
	public ArrayAccessReference(Context context, ArrayAccessExpression expression) {
		super(context);
		
		Objects.requireNonNull(expression);
		
		this.expression = expression;
	}
	
	public ArrayAccessExpression getExpression() {
		return expression;
	}
	
	@Override
	public TypeReference getType() {
		return expression.getType();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.ARRAY_ACCESS_REFERENCE;
	}

	@Override
	public <T, R> R visit(VariableReferenceVisitor<T, R> visitor, T param) {
		return visitor.onArrayAccessReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
