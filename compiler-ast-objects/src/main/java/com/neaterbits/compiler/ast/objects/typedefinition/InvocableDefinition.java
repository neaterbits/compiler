package com.neaterbits.compiler.ast.objects.typedefinition;

import java.util.List;

import com.neaterbits.compiler.ast.objects.Keyword;
import com.neaterbits.compiler.util.name.InvocableName;
import com.neaterbits.util.parse.context.Context;

public abstract class InvocableDefinition<T extends InvocableName, DECLARATION_NAME extends DeclarationName<T>>
		extends ComplexTypeDefinition<T, DECLARATION_NAME> {

	public InvocableDefinition(Context context, Keyword typeKeyword, DECLARATION_NAME name, List<ComplexMemberDefinition> members) {
		super(context, typeKeyword, name, members);
	}
}
