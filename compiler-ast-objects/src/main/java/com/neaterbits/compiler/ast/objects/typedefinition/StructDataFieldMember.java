package com.neaterbits.compiler.ast.objects.typedefinition;

import com.neaterbits.compiler.ast.objects.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.objects.FieldNameDeclaration;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class StructDataFieldMember extends DataFieldMember {
	
	public StructDataFieldMember(Context context, TypeReference type, FieldNameDeclaration name) {
		super(context, type, name);
	}

	@Override
	protected ComplexMemberType getMemberType() {
		return ComplexMemberType.FIELD;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.STRUCT_DATA_FIELD_MEMBER;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onStructDataFieldMember(this, param);
	}
}