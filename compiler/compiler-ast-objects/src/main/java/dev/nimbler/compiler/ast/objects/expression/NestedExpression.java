package dev.nimbler.compiler.ast.objects.expression;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.variables.ResolvedPrimary;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class NestedExpression extends ResolvedPrimary {

	private final ASTSingle<Expression> expression;
	
	public NestedExpression(Context context, Expression expression) {
		super(context);
		
		Objects.requireNonNull(expression);
		
		this.expression = makeSingle(expression);
	}

	public Expression getExpression() {
		return expression.get();
	}

	@Override
	public TypeReference getType() {
		return expression.get().getType();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.NESTED_EXPRESSION;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onNestedExpression(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(expression, recurseMode, iterator);
	}
}
