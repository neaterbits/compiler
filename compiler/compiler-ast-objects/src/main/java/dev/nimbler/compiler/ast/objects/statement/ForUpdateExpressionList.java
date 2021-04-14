package dev.nimbler.compiler.ast.objects.statement;

import java.util.List;
import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class ForUpdateExpressionList extends BaseASTElement {

	private final ASTList<Expression> expressions;

	public ForUpdateExpressionList(Context context, List<Expression> expressions) {
		super(context);
		
		Objects.requireNonNull(expressions);
		
		if (expressions.isEmpty()) {
			throw new IllegalStateException("No expressions");
		}
		
		this.expressions = makeList(expressions);
	}

	public ASTList<Expression> getExpressions() {
		return expressions;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.FOR_EXPRESSION_LIST;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(expressions, recurseMode, iterator);
	}
}
