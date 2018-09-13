package com.neaterbits.compiler.common.ast.statement;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.CompilationCode;

public abstract class Statement extends CompilationCode {

	public Statement(Context context) {
		super(context);
	}
}
