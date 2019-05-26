package com.neaterbits.compiler.ast.type.complex;

import java.util.List;

import com.neaterbits.compiler.ast.type.CompleteName;
import com.neaterbits.compiler.ast.type.TypeVisitor;
import com.neaterbits.compiler.ast.typedefinition.ClassDeclarationName;
import com.neaterbits.compiler.ast.typedefinition.ClassDefinition;
import com.neaterbits.compiler.util.name.ClassName;
import com.neaterbits.compiler.util.name.DefinitionName;
import com.neaterbits.compiler.util.name.NamespaceReference;

public final class ClassType extends InvocableType<ClassName, ClassDeclarationName, ClassDefinition> {
	
	public ClassType(NamespaceReference namespace, List<DefinitionName> outerTypes, ClassDefinition definition) {
		super(
				new CompleteName(namespace, outerTypes, definition.getTypeName()),
				true,
				definition);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onClass(this, param);
	}
}
