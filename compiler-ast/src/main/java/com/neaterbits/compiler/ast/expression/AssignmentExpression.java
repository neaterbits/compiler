package com.neaterbits.compiler.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.ast.variables.VariableReference;
import com.neaterbits.compiler.util.Context;

public class AssignmentExpression extends Expression {
	private final ASTSingle<VariableReference> variable;
	private final ASTSingle<Expression> expression;
	
	public AssignmentExpression(Context context, VariableReference variable, Expression expression) {
		super(context);
		
		Objects.requireNonNull(variable);
		Objects.requireNonNull(expression);
		
		this.variable = makeSingle(variable);
		this.expression = makeSingle(expression);
	}

	public VariableReference getVariable() {
		return variable.get();
	}

	public Expression getExpression() {
		return expression.get();
	}

	@Override
	public BaseType getType() {
		return variable.get().getType();
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onAssignment(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(variable, recurseMode, iterator);
		doIterate(expression, recurseMode, iterator);
	}
}
