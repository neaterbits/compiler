package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.Collections;
import java.util.List;

import com.neaterbits.compiler.common.Context;

public abstract class ComplexTypeDefinition extends TypeDefinition {

	private final List<ComplexMemberDefinition> members;
	
	protected ComplexTypeDefinition(Context context, DefinitionName name, List<ComplexMemberDefinition> members) {
		super(context, name);
		
		this.members = Collections.unmodifiableList(members);
	}

	public final List<ComplexMemberDefinition> getMembers() {
		return members;
	}
}
