package dev.nimbler.compiler.ast.objects.typedefinition;

import java.util.List;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.Keyword;
import dev.nimbler.compiler.util.name.InvocableName;

public abstract class InvocableDefinition<T extends InvocableName, DECLARATION_NAME extends DeclarationName<T>>
		extends ComplexTypeDefinition<T, DECLARATION_NAME> {

	public InvocableDefinition(Context context, Keyword typeKeyword, DECLARATION_NAME name, List<ComplexMemberDefinition> members) {
		super(context, typeKeyword, name, members);
	}
}
