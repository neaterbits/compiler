package com.neaterbits.compiler.common.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public final class ConstantSwitchCaseLabel extends SwitchCaseLabel {

	private final ASTSingle<Expression> constant;

	public ConstantSwitchCaseLabel(Context context, Expression constant) {
		super(context);

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
		doIterate(constant, recurseMode, iterator);
	}
}
