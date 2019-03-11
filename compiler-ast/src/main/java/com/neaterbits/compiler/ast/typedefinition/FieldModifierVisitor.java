package com.neaterbits.compiler.ast.typedefinition;

import com.neaterbits.compiler.ast.statement.FieldTransient;
import com.neaterbits.compiler.ast.statement.FieldVolatile;
import com.neaterbits.compiler.ast.statement.Mutability;

public interface FieldModifierVisitor<T, R> {

	R onFieldVisibility(FieldVisibility visibility, T param);
	
	R onFieldMutability(Mutability mutability, T param);

	R onStatic(FieldStatic fieldStatic, T param);

	R onTransient(FieldTransient fieldTransient, T param);

	R onVolatile(FieldVolatile fieldVolatile, T param);
	
}