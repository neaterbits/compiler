package com.neaterbits.compiler.common.ast.type.complex;

import com.neaterbits.compiler.common.ast.type.TypeVisitor;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;

public final class ClassType extends ComplexType {
	
	public ClassType(ClassDefinition definition) {
		super(definition.getName(), true);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onClass(this, param);
	}
}
