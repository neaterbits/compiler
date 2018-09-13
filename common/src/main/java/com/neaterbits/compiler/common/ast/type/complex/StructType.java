package com.neaterbits.compiler.common.ast.type.complex;

import com.neaterbits.compiler.common.ast.type.TypeVisitor;
import com.neaterbits.compiler.common.ast.typedefinition.StructDefinition;

public final class StructType extends ComplexType {

	public StructType(StructDefinition definition, boolean nullable) {
		super(definition.getName(), nullable);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onStruct(this, param);
	}
}
