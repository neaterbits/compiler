package com.neaterbits.compiler.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.Keyword;
import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.util.Context;

public final class ConstantSwitchCaseLabel extends SwitchCaseLabel {

	private final ASTSingle<Expression> constant;

	public ConstantSwitchCaseLabel(Context context, Keyword keyword, Expression constant) {
		super(context, keyword);

		Objects.requireNonNull(constant);
		
		this.constant = makeSingle(constant);
	}

	public Expression getConstant() {
		return constant.get();
	}

	@Override
	public <T, R> R visit(SwitchCaseLabelVisitor<T, R> visitor, T param) {
		return visitor.onConstant(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
		super.doRecurse(recurseMode, iterator);
		
		doIterate(constant, recurseMode, iterator);
	}
}
