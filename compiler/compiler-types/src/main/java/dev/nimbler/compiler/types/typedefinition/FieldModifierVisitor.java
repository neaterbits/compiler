package dev.nimbler.compiler.types.typedefinition;

import dev.nimbler.compiler.types.statement.ASTMutability;

public interface FieldModifierVisitor<T, R> {

	R onFieldVisibility(FieldVisibility visibility, T param);
	
	R onFieldMutability(ASTMutability mutability, T param);

	R onStatic(FieldStatic fieldStatic, T param);

	R onTransient(FieldTransient fieldTransient, T param);

	R onVolatile(FieldVolatile fieldVolatile, T param);
	
}
