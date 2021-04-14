package dev.nimbler.compiler.ast.objects.type.complex;

import java.util.List;

import dev.nimbler.compiler.ast.objects.type.CompleteName;
import dev.nimbler.compiler.ast.objects.type.TypeVisitor;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDeclarationName;
import dev.nimbler.compiler.ast.objects.typedefinition.EnumDefinition;
import dev.nimbler.compiler.util.name.ClassName;
import dev.nimbler.compiler.util.name.DefinitionName;
import dev.nimbler.compiler.util.name.NamespaceReference;

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
