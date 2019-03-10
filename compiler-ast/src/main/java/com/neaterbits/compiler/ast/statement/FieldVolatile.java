package com.neaterbits.compiler.ast.statement;

import com.neaterbits.compiler.ast.typedefinition.FieldModifier;
import com.neaterbits.compiler.ast.typedefinition.FieldModifierVisitor;

public final class FieldVolatile implements FieldModifier {

	@Override
	public <T, R> R visit(FieldModifierVisitor<T, R> visitor, T param) {
		return visitor.onVolatile(this, param);
	}
}
