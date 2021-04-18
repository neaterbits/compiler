package dev.nimbler.compiler.ast.objects.typedefinition;

import java.util.List;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.CompilationCodeVisitor;
import dev.nimbler.compiler.ast.objects.Keyword;
import dev.nimbler.compiler.types.ParseTreeElement;

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
