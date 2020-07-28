package com.neaterbits.compiler.ast.objects.statement;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.expression.Expression;
import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

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
