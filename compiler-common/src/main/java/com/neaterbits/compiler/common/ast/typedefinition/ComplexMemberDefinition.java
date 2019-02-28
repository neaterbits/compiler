package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.CompilationCode;

public abstract class ComplexMemberDefinition extends CompilationCode {

	protected abstract ComplexMemberType getMemberType();

	public final boolean isMethod() {
		return getMemberType() == ComplexMemberType.CLASS_METHOD || getMemberType() == ComplexMemberType.INTERFACE_METHOD;
	}
	
	protected ComplexMemberDefinition(Context context) {
		super(context);
	}
}
