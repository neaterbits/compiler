package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;

public abstract class DataFieldMember extends ComplexMemberDefinition {

	private final TypeReference type;
	private final String name;

	public DataFieldMember(Context context, TypeReference type, String name) {
		super(context);
	
		this.type = type;
		this.name = name;
	}

	public final TypeReference getType() {
		return type;
	}

	public final String getName() {
		return name;
	}
}
