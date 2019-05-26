package com.neaterbits.compiler.ast.type.complex;

import java.util.List;

import com.neaterbits.compiler.ast.type.CompleteName;
import com.neaterbits.compiler.ast.type.TypeVisitor;
import com.neaterbits.compiler.ast.typedefinition.ClassDeclarationName;
import com.neaterbits.compiler.ast.typedefinition.EnumDefinition;
import com.neaterbits.compiler.util.name.ClassName;
import com.neaterbits.compiler.util.name.DefinitionName;
import com.neaterbits.compiler.util.name.NamespaceReference;

public final class EnumType extends InvocableType<ClassName, ClassDeclarationName, EnumDefinition> {

	public EnumType(NamespaceReference namespace, List<DefinitionName> outerTypes, EnumDefinition enumDefinition) {
		super(
				new CompleteName(namespace, outerTypes, enumDefinition.getTypeName()),
				true,
				enumDefinition);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onEnum(this, param);
	}
}
