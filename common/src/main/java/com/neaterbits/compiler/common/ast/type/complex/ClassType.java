package com.neaterbits.compiler.common.ast.type.complex;

import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.type.TypeVisitor;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;

public final class ClassType extends ComplexType {
	
	private final ClassDefinition classDefinition;
	
	public ClassType(NamespaceReference namespace, ClassDefinition definition) {
		super(namespace, definition.getName(), true);
		
		this.classDefinition = definition;
	}

	public ClassDefinition getClassDefinition() {
		return classDefinition;
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onClass(this, param);
	}
}
