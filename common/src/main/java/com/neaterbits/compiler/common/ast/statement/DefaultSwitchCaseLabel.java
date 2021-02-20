package com.neaterbits.compiler.common.ast.statement;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;

public final class DefaultSwitchCaseLabel extends SwitchCaseLabel {

	public DefaultSwitchCaseLabel(Context context) {
		super(context);
	}

	@Override
	public <T, R> R visit(SwitchCaseLabelVisitor<T, R> visitor, T param) {
		return visitor.onDefault(this, param);
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		
	}
}
