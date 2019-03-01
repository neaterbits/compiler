package com.neaterbits.compiler.common.ast.type.complex;

import java.util.List;

import com.neaterbits.compiler.common.ast.NamespaceReference;
import com.neaterbits.compiler.common.ast.type.CompleteName;
import com.neaterbits.compiler.common.ast.type.TypeVisitor;
import com.neaterbits.compiler.common.ast.typedefinition.DefinitionName;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceDeclarationName;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceDefinition;
import com.neaterbits.compiler.common.ast.typedefinition.InterfaceName;

public final class InterfaceType extends InvocableType<InterfaceName, InterfaceDeclarationName, InterfaceDefinition> {

	public InterfaceType(NamespaceReference namespace, List<DefinitionName> outerTypes, InterfaceDefinition interfaceDefinition) {
		super(
				new CompleteName(namespace, outerTypes, interfaceDefinition.getTypeName()),
				true,
				interfaceDefinition);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onInterface(this, param);
	}
}
