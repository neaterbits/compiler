package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.common.Context;

public abstract class InvocableDefinition extends ComplexTypeDefinition {

	public InvocableDefinition(Context context, InvocableName name, List<ComplexMemberDefinition> members) {
		super(context, name, members);
	}
}
