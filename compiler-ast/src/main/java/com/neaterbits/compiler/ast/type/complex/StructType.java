package com.neaterbits.compiler.ast.type.complex;

import com.neaterbits.compiler.ast.type.CompleteName;
import com.neaterbits.compiler.ast.type.TypeVisitor;
import com.neaterbits.compiler.ast.typedefinition.StructDeclarationName;
import com.neaterbits.compiler.ast.typedefinition.StructDefinition;
import com.neaterbits.compiler.ast.typedefinition.StructName;

public final class StructType extends ComplexType<StructName, StructDeclarationName, StructDefinition> {

	public StructType(StructDefinition definition) {
		super(new CompleteName(null, null, definition.getName().getName()), true, definition);
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onStruct(this, param);
	}
}
