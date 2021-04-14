package dev.nimbler.compiler.ast.objects.type.complex;

import java.util.List;

import dev.nimbler.compiler.ast.objects.type.CompleteName;
import dev.nimbler.compiler.ast.objects.type.TypeVisitor;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDeclarationName;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassDefinition;
import dev.nimbler.compiler.util.name.ClassName;
import dev.nimbler.compiler.util.name.DefinitionName;
import dev.nimbler.compiler.util.name.NamespaceReference;

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
