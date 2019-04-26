package com.neaterbits.compiler.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.Keyword;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public class StructDefinition extends ComplexTypeDefinition<StructName, StructDeclarationName> {

	public StructDefinition(Context context, Keyword structKeyword, StructDeclarationName name, List<ComplexMemberDefinition> members) {
		super(context, structKeyword, name, members);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.STRUCT_DEFINITION;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onStructDefinition(this, param);
	}
}
