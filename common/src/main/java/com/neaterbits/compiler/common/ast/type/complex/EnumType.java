package com.neaterbits.compiler.common.ast.type.complex;

import com.neaterbits.compiler.common.ast.type.TypeVisitor;
import com.neaterbits.compiler.common.ast.typedefinition.EnumDefinition;

public final class EnumType extends ComplexType {

	public EnumType(EnumDefinition enumDefinition) {
		super(enumDefinition.getName(), true);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onEnum(this, param);
	}
}
