package com.neaterbits.compiler.ast.typedefinition;

import com.neaterbits.compiler.ast.CompilationCode;
import com.neaterbits.compiler.util.Context;

public abstract class ComplexMemberDefinition extends CompilationCode {

	protected abstract ComplexMemberType getMemberType();

	public final boolean isMethod() {
		return getMemberType() == ComplexMemberType.CLASS_METHOD || getMemberType() == ComplexMemberType.INTERFACE_METHOD;
	}
	
	protected ComplexMemberDefinition(Context context) {
		super(context);
	}
}
