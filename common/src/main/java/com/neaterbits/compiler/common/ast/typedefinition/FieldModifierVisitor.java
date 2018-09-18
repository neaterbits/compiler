package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.ast.statement.FieldTransient;
import com.neaterbits.compiler.common.ast.statement.FieldVolatile;
import com.neaterbits.compiler.common.ast.statement.Mutability;

public interface FieldModifierVisitor<T, R> {

	R onFieldVisibility(FieldVisibility visibility, T param);
	
	R onFieldMutability(Mutability mutability, T param);

	R onStatic(FieldStatic fieldStatic, T param);

	R onTransient(FieldTransient fieldTransient, T param);

	R onVolatile(FieldVolatile fieldVolatile, T param);
	
}
