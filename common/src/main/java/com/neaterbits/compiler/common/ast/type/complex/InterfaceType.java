package com.neaterbits.compiler.common.ast.type.complex;

import com.neaterbits.compiler.common.ast.type.TypeVisitor;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceDefinition;

public final class InterfaceType extends ComplexType {

	public InterfaceType(InterfaceDefinition interfaceDefinition) {
		super(interfaceDefinition.getName(), true);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onInterface(this, param);
	}
}
