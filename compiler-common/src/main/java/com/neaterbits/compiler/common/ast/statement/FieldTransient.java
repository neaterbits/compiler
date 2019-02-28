package com.neaterbits.compiler.common.ast.statement;

import com.neaterbits.compiler.common.ast.typedefinition.FieldModifier;
import com.neaterbits.compiler.common.ast.typedefinition.FieldModifierVisitor;

public final class FieldTransient implements FieldModifier {

	@Override
	public <T, R> R visit(FieldModifierVisitor<T, R> visitor, T param) {
		return visitor.onTransient(this, param);
	}
}
