package com.neaterbits.compiler.common.ast.type.complex;

import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.type.TypeVisitor;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceDefinition;

public final class InterfaceType extends ComplexType {

	public InterfaceType(NamespaceReference namespace, InterfaceDefinition interfaceDefinition) {
		super(namespace, interfaceDefinition.getName(), true);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onInterface(this, param);
	}
}
