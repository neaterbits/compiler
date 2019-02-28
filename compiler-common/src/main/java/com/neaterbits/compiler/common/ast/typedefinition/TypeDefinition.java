package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.CompilationCode;
import com.neaterbits.compiler.common.ast.type.BaseTypeName;

public abstract class TypeDefinition<T extends BaseTypeName> extends CompilationCode {
	
	private final T name;
	
	protected TypeDefinition(Context context, T name) {
		super(context);
		
		this.name = name;
	}

	public final T getName() {
		return name;
	}
}
