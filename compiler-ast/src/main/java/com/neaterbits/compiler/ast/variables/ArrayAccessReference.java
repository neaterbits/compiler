package com.neaterbits.compiler.ast.variables;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.expression.ArrayAccessExpression;
import com.neaterbits.compiler.ast.type.BaseType;
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
	public BaseType getType() {
		return expression.getType();
	}

	@Override
	public <T, R> R visit(VariableReferenceVisitor<T, R> visitor, T param) {
		return visitor.onArrayAccessReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}