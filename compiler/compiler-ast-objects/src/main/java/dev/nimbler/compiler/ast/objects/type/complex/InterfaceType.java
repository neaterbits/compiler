package dev.nimbler.compiler.ast.objects.type.complex;

import java.util.List;

import dev.nimbler.compiler.ast.objects.type.CompleteName;
import dev.nimbler.compiler.ast.objects.type.TypeVisitor;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceDeclarationName;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.InterfaceName;
import dev.nimbler.compiler.util.name.DefinitionName;
import dev.nimbler.compiler.util.name.NamespaceReference;

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
