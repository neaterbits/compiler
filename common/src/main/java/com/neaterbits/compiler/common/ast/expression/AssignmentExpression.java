package com.neaterbits.compiler.common.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.list.ASTSingle;
import com.neaterbits.compiler.common.ast.variables.VariableReference;

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
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onAssignment(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(variable, recurseMode, iterator);
		doIterate(expression, recurseMode, iterator);
	}
}
