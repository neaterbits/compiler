package com.neaterbits.compiler.model.common;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.types.MethodVariant;
import com.neaterbits.compiler.types.Mutability;
import com.neaterbits.compiler.types.Visibility;

public interface TypeMemberVisitor extends TypeVisitor {

	void onField(
			CharSequence name,
			TypeName type,
			int numArrayDimensions,
			boolean isStatic,
			Visibility visibility,
			Mutability mutability,
			boolean isVolatile,
			boolean isTransient,
			int indexInType);
	
    void onMethod(
            String name,
            MethodVariant methodVariant,
            TypeName returnType,
            TypeName [] parameterTypes,
            int indexInType);
}
