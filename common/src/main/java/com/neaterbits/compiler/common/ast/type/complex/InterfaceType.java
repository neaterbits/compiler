package com.neaterbits.compiler.common.ast.type.complex;

import java.util.List;

import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.type.FullTypeName;
import com.neaterbits.compiler.common.ast.type.TypeName;
import com.neaterbits.compiler.common.ast.type.TypeVisitor;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceDefinition;

public final class InterfaceType extends ComplexType<InterfaceDefinition> {

	public InterfaceType(NamespaceReference namespace, List<TypeName> outerTypes, InterfaceDefinition interfaceDefinition) {
		super(
				new FullTypeName(namespace, outerTypes, interfaceDefinition.getName()),
				true,
				interfaceDefinition);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onInterface(this, param);
	}
}
