package com.neaterbits.compiler.ast.block;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.expression.ParameterList;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.statement.Statement;
import com.neaterbits.compiler.ast.statement.StatementVisitor;
import com.neaterbits.compiler.util.Context;

public final class ConstructorInvocationStatement extends Statement {

	private final ConstructorInvocation type;
	
	private final ASTSingle<Expression> expression;
	private final ASTSingle<ParameterList> parameters;
	
	public ConstructorInvocationStatement(Context context, ConstructorInvocation type, Expression expression, ParameterList parameters) {
		super(context);

		Objects.requireNonNull(type);
		Objects.requireNonNull(parameters);

		if (type == ConstructorInvocation.EXPRESSIONNAME_SUPER || type == ConstructorInvocation.PRIMARY_SUPER) {
			Objects.requireNonNull(expression);
		}
		
		this.type = type;
		this.expression = expression != null ? makeSingle(expression) : null;
		this.parameters = makeSingle(parameters);
	}

	public ConstructorInvocation getType() {
		return type;
	}

	public Expression getExpression() {
		return expression != null ? expression.get() : null;
	}
	
	public ParameterList getParameters() {
		return parameters.get();
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onConstructorInvocation(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
		if (expression != null) {
			doIterate(expression, recurseMode, iterator);
		}
		
		doIterate(parameters, recurseMode, iterator);
	}
}
