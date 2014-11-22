package com.neaterbits.compiler.common.ast.type.complex;

import java.util.List;

import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.type.TypeVisitor;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.DefinitionName;

public final class ClassType extends InvocableType<ClassDefinition> {
	
	public ClassType(NamespaceReference namespace, List<DefinitionName> outerTypes, ClassDefinition definition) {
		super(
				new CompleteName(namespace, outerTypes, definition.getName()),
				true,
				definition);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onClass(this, param);
	}
}
