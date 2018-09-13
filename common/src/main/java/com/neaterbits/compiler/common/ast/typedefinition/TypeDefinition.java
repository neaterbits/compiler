package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.CompilationCode;

public abstract class TypeDefinition extends CompilationCode {
	
	private final String name;
	
	protected TypeDefinition(Context context, String name) {
		super(context);
		
		this.name = name;
	}
}
