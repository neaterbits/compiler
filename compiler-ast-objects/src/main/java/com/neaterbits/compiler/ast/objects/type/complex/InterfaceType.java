package com.neaterbits.compiler.ast.objects.type.complex;

import java.util.List;

import com.neaterbits.compiler.ast.objects.type.CompleteName;
import com.neaterbits.compiler.ast.objects.type.TypeVisitor;
import com.neaterbits.compiler.ast.objects.typedefinition.InterfaceDeclarationName;
import com.neaterbits.compiler.ast.objects.typedefinition.InterfaceDefinition;
import com.neaterbits.compiler.ast.objects.typedefinition.InterfaceName;
import com.neaterbits.compiler.util.name.DefinitionName;
import com.neaterbits.compiler.util.name.NamespaceReference;

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
