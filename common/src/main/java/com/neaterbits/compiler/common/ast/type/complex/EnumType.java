package com.neaterbits.compiler.common.ast.type.complex;

import java.util.List;

import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.type.FullTypeName;
import com.neaterbits.compiler.common.ast.type.TypeName;
import com.neaterbits.compiler.common.ast.type.TypeVisitor;
import com.neaterbits.compiler.common.ast.typedefinition.EnumDefinition;

public final class EnumType extends ComplexType<EnumDefinition> {

	public EnumType(NamespaceReference namespace, List<TypeName> outerTypes, EnumDefinition enumDefinition) {
		super(
				new FullTypeName(namespace, outerTypes, enumDefinition.getName()),
				true,
				enumDefinition);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onEnum(this, param);
	}
}
