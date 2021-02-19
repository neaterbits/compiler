package com.neaterbits.compiler.common.ast.statement;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.list.ASTList;

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
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		doIterate(expressions, recurseMode, visitor);
	}
}
