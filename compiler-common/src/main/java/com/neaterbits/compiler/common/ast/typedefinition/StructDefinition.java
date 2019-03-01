package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;

public class StructDefinition extends ComplexTypeDefinition<StructName, StructDeclarationName> {

	public StructDefinition(Context context, StructDeclarationName name, List<ComplexMemberDefinition> members) {
		super(context, name, members);
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onStructDefinition(this, param);
	}
}
