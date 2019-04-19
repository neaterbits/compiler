package com.neaterbits.compiler.resolver;

import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.model.Mutability;
import com.neaterbits.compiler.util.model.Visibility;

@FunctionalInterface
public interface ASTFieldVisitor {

	void onField(
			String name,
			TypeName type,
			int numArrayDimensions,
			boolean isStatic,
			Visibility visibility,
			Mutability mutability,
			boolean isVolatile,
			boolean isTransient,
			int indexInType);
	
}
