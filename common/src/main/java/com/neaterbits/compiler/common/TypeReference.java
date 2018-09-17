package com.neaterbits.compiler.common;

import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.type.BaseType;

/**
 * For resolving types later, eg. when referencing a class that has not been parsed yet
 */

public abstract class TypeReference extends BaseASTElement {

	public TypeReference(Context context) {
		super(context);
	}

	public abstract BaseType getType();
	
}
