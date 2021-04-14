package dev.nimbler.compiler.model.common;

import dev.nimbler.language.common.types.MethodVariant;
import dev.nimbler.language.common.types.Mutability;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.types.Visibility;

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
