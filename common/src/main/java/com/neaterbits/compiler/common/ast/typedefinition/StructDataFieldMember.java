package com.neaterbits.compiler.common.ast.typedefinition;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;

public final class StructDataFieldMember extends DataFieldMember {
	
	public StructDataFieldMember(Context context, TypeReference type, FieldName name) {
		super(context, type, name);
	}

	@Override
	protected ComplexMemberType getMemberType() {
		return ComplexMemberType.FIELD;
	}


	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onStructDataFieldMember(this, param);
	}
}
