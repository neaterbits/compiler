package dev.nimbler.compiler.ast.objects.typereference;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.language.common.types.TypeName;

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
