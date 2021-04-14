package dev.nimbler.compiler.ast.objects.statement;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.expression.AssignmentExpression;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class AssignmentStatement extends Statement {

	private final ASTSingle<AssignmentExpression> expression;

	public AssignmentStatement(Context context, AssignmentExpression expression) {
		super(context);

		Objects.requireNonNull(expression);

		this.expression = makeSingle(expression);
	}

	public AssignmentExpression getExpression() {
		return expression.get();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.ASSIGNMENT_STATEMENT;
	}

	@Override
	public <T, R> R visit(StatementVisitor<T, R> visitor, T param) {
		return visitor.onAssignment(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(expression, recurseMode, iterator);
	}
}
