package com.neaterbits.compiler.ast.objects.typedefinition;

import java.util.List;

import com.neaterbits.compiler.ast.objects.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.objects.Keyword;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.Context;

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
