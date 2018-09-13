package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.CompilationCode;

public abstract class ComplexMemberDefinition extends CompilationCode {

	protected ComplexMemberDefinition(Context context) {
		super(context);
	}
}
