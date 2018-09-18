package com.neaterbits.compiler.common.ast.statement;

import com.neaterbits.compiler.common.ast.typedefinition.FieldModifier;
import com.neaterbits.compiler.common.ast.typedefinition.FieldModifierVisitor;

public final class FieldVolatile implements FieldModifier {

	@Override
	public <T, R> R visit(FieldModifierVisitor<T, R> visitor, T param) {
		return visitor.onVolatile(this, param);
	}
}
