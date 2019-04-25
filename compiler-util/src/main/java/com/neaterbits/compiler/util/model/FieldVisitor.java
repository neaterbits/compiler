package com.neaterbits.compiler.util.model;

import com.neaterbits.compiler.util.TypeName;

@FunctionalInterface
public interface FieldVisitor {

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
