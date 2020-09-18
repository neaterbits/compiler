package com.neaterbits.compiler.types.typedefinition;

import com.neaterbits.compiler.types.statement.ASTMutability;

public interface FieldModifierVisitor<T, R> {

	R onFieldVisibility(FieldVisibility visibility, T param);
	
	R onFieldMutability(ASTMutability mutability, T param);

	R onStatic(FieldStatic fieldStatic, T param);

	R onTransient(FieldTransient fieldTransient, T param);

	R onVolatile(FieldVolatile fieldVolatile, T param);
	
}
