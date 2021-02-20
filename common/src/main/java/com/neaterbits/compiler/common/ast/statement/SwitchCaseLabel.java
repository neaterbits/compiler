package com.neaterbits.compiler.common.ast.statement;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.BaseASTElement;

public abstract class SwitchCaseLabel extends BaseASTElement {

	public abstract <T, R> R visit(SwitchCaseLabelVisitor<T, R> visitor, T param);
	
	public SwitchCaseLabel(Context context) {
		super(context);
	}
}
