package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;

public class ClassDataFieldMember extends DataFieldMember {

	public ClassDataFieldMember(Context context, TypeReference type, String name) {
		super(context, type, name);
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return null;
	}
}
