package com.neaterbits.compiler.common.ast.statement;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
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
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		doIterate(constant, recurseMode, visitor);
	}
}
