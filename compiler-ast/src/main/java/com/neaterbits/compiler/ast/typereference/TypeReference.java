package com.neaterbits.compiler.ast.typereference;

import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.TypeName;

/**
 * For resolving types later, eg. when referencing a class that has not been parsed yet
 */

public abstract class TypeReference extends BaseASTElement {

	public TypeReference(Context context) {
		super(context);
	}

	public abstract BaseType getType();
	
	public abstract String getDebugName();
	
	public abstract TypeName getTypeName();
	
	public abstract <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param);
	
}
