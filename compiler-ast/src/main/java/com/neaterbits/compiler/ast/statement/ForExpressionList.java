package com.neaterbits.compiler.ast.statement;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class ForExpressionList extends BaseASTElement {

	private final ASTList<Expression> expressions;

	public ForExpressionList(Context context, List<Expression> expressions) {
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
