package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.CompilationCode;
import com.neaterbits.compiler.common.ast.type.TypeName;

public abstract class TypeDefinition extends CompilationCode {
	
	private final TypeName name;
	
	protected TypeDefinition(Context context, TypeName name) {
		super(context);
		
		this.name = name;
	}

	public final TypeName getName() {
		return name;
	}
}
