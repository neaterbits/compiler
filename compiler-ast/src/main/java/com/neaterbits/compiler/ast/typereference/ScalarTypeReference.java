package com.neaterbits.compiler.ast.typereference;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.TypeName;

public final class ScalarTypeReference extends BuiltinTypeReference {

	public ScalarTypeReference(Context context, TypeName type) {
		super(context, type);
	}

	@Override
	public boolean isScalar() {
		return true;
	}
	
	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onScalarTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
