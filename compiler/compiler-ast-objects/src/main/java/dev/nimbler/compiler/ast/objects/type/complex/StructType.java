package dev.nimbler.compiler.ast.objects.type.complex;

import dev.nimbler.compiler.ast.objects.type.CompleteName;
import dev.nimbler.compiler.ast.objects.type.TypeVisitor;
import dev.nimbler.compiler.ast.objects.typedefinition.StructDeclarationName;
import dev.nimbler.compiler.ast.objects.typedefinition.StructDefinition;
import dev.nimbler.compiler.ast.objects.typedefinition.StructName;

public final class StructType extends ComplexType<StructName, StructDeclarationName, StructDefinition> {

	public StructType(StructDefinition definition) {
		super(new CompleteName(null, null, definition.getName().getName()), true, definition);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onStruct(this, param);
	}
}
