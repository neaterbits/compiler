package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.type.BaseType;

public class DataFieldDefinition extends BaseASTElement {

	private final BaseType type;
	private final String name;
	
	public DataFieldDefinition(Context context, BaseType type, String name) {
		super(context);
	
		this.type = type;
		this.name = name;
	}
}
