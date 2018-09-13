package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.common.Context;

public class ClassDefinition extends ComplexDefinition {

	public ClassDefinition(Context context, String name, List<ComplexMemberDefinition> members) {
		super(context, name, members);
	}
}
