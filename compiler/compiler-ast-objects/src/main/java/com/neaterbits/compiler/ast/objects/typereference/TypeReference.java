package com.neaterbits.compiler.ast.objects.typereference;

import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.language.common.types.TypeName;
import com.neaterbits.util.parse.context.Context;

/**
 * For resolving types later, eg. when referencing a class that has not been parsed yet
 */

public abstract class TypeReference extends BaseASTElement {

	public TypeReference(Context context) {
		super(context);
	}

	public abstract String getDebugName();
	
	public abstract TypeName getTypeName();
	
	public abstract <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param);
	
}
