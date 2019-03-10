package com.neaterbits.compiler.ast.typedefinition;

import com.neaterbits.compiler.ast.CompilationCode;
import com.neaterbits.compiler.ast.type.BaseTypeName;
import com.neaterbits.compiler.util.Context;

public abstract class TypeDefinition<T extends BaseTypeName, DECLARATION_NAME extends DeclarationName<T>> extends CompilationCode {
	
	private final DECLARATION_NAME name;
	
	protected TypeDefinition(Context context, DECLARATION_NAME name) {
		super(context);
		
		this.name = name;
	}

	public final DECLARATION_NAME getName() {
		return name;
	}

	public final BaseTypeName getTypeName() {
		return name.getName();
	}
	
	public final String getNameString() {
		return name.getName().getName();
	}
}
