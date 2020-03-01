package com.neaterbits.compiler.ast.objects.expression;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.ast.objects.variables.VariableReference;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

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
	public TypeReference getType() {
		return variable.get().getType();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.ASSIGNMENT_EXPRESSION;
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
