package com.neaterbits.compiler.common.ast.type.complex;

import java.util.List;

import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.type.TypeVisitor;
import com.neaterbits.compiler.common.ast.typedefinition.ClassDeclarationName;
import com.neaterbits.compiler.common.ast.typedefinition.ClassName;
import com.neaterbits.compiler.common.ast.typedefinition.DefinitionName;
import com.neaterbits.compiler.common.ast.typedefinition.EnumDefinition;

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
