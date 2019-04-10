package com.neaterbits.compiler.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.ast.Keyword;
import com.neaterbits.compiler.util.Context;

public abstract class InvocableDefinition<T extends InvocableName, DECLARATION_NAME extends DeclarationName<T>>
		extends ComplexTypeDefinition<T, DECLARATION_NAME> {

	public InvocableDefinition(Context context, Keyword typeKeyword, DECLARATION_NAME name, List<ComplexMemberDefinition> members) {
		super(context, typeKeyword, name, members);
	}
}
