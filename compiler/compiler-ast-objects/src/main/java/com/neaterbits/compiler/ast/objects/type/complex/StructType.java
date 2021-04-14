package com.neaterbits.compiler.ast.objects.type.complex;

import com.neaterbits.compiler.ast.objects.type.CompleteName;
import com.neaterbits.compiler.ast.objects.type.TypeVisitor;
import com.neaterbits.compiler.ast.objects.typedefinition.StructDeclarationName;
import com.neaterbits.compiler.ast.objects.typedefinition.StructDefinition;
import com.neaterbits.compiler.ast.objects.typedefinition.StructName;

public final class StructType extends ComplexType<StructName, StructDeclarationName, StructDefinition> {

	public StructType(StructDefinition definition) {
		super(new CompleteName(null, null, definition.getName().getName()), true, definition);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onStruct(this, param);
	}
}
