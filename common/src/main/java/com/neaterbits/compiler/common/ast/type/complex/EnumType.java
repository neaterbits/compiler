package com.neaterbits.compiler.common.ast.type.complex;

import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.type.TypeVisitor;
import com.neaterbits.compiler.common.ast.typedefinition.EnumDefinition;

public final class EnumType extends ComplexType {

	public EnumType(NamespaceReference namespace, EnumDefinition enumDefinition) {
		super(namespace, enumDefinition.getName(), true);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onEnum(this, param);
	}
}
