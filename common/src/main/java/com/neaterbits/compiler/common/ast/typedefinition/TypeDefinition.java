package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.CompilationCode;
import com.neaterbits.compiler.common.ast.type.TypeName;

public abstract class TypeDefinition<T extends TypeName> extends CompilationCode {
	
	private final T name;
	
	protected TypeDefinition(Context context, T name) {
		super(context);
		
		this.name = name;
	}

	public final T getName() {
		return name;
	}
}
