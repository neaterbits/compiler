package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.common.Context;

public abstract class ComplexDefinition extends TypeDefinition {

	private final List<ComplexMemberDefinition> members;
	
	protected ComplexDefinition(Context context, String name, List<ComplexMemberDefinition> members) {
		super(context, name);
		
		this.members = members;
	}
}
