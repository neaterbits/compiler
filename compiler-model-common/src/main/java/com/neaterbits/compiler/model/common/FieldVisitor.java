package com.neaterbits.compiler.model.common;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.types.Mutability;
import com.neaterbits.compiler.types.Visibility;

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
