package com.neaterbits.compiler.common.ast.type.complex;

import java.util.List;

import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.type.FullTypeName;
import com.neaterbits.compiler.common.ast.type.TypeName;
import com.neaterbits.compiler.common.ast.type.TypeVisitor;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;

public final class ClassType extends ComplexType<ClassDefinition> {
	
	public ClassType(NamespaceReference namespace, List<TypeName> outerTypes, ClassDefinition definition) {
		super(
				new FullTypeName(namespace, outerTypes, definition.getName()),
				true,
				definition);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onClass(this, param);
	}
}
