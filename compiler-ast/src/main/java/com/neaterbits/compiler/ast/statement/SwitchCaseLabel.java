package com.neaterbits.compiler.ast.statement;

import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.util.Context;

public abstract class SwitchCaseLabel extends BaseASTElement {

	public abstract <T, R> R visit(SwitchCaseLabelVisitor<T, R> visitor, T param);
	
	public SwitchCaseLabel(Context context) {
		super(context);
	}
}
