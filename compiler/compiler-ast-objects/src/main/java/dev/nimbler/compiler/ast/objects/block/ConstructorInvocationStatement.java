package dev.nimbler.compiler.ast.objects.block;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.expression.ParameterList;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.statement.Statement;
import dev.nimbler.compiler.ast.objects.statement.StatementVisitor;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.block.ConstructorInvocation;

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
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CONSTRUCTOR_INVOCATION_STATEMENT;
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
