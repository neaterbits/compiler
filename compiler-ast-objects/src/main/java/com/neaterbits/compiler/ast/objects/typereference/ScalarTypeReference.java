package com.neaterbits.compiler.ast.objects.typereference;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.TypeName;

public final class ScalarTypeReference extends BuiltinTypeReference {

	public ScalarTypeReference(Context context, int typeNo, TypeName type) {
		super(context, typeNo, type);
	}

	@Override
	public boolean isScalar() {
		return true;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.SCALAR_TYPE_REFERENCE;
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onScalarTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

	}
}
