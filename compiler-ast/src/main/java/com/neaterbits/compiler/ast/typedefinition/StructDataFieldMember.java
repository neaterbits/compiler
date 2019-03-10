package com.neaterbits.compiler.ast.typedefinition;

import com.neaterbits.compiler.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

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
