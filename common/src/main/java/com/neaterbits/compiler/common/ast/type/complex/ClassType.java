package com.neaterbits.compiler.common.ast.type.complex;

import com.neaterbits.compiler.common.ast.type.TypeVisitor;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;

public class ClassType extends ComplexType {
	
	public ClassType(ClassDefinition definition, boolean nullable) {
		super(definition.getName(), nullable);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return null;
	}
}
