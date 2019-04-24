package com.neaterbits.compiler.ast.typereference;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.TypeName;

public final class UnnamedVoidTypeReference extends TypeReference {

	public UnnamedVoidTypeReference(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getDebugName() {
		return "unnamed_void";
	}

	@Override
	public TypeName getTypeName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onUnnamedVoid(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
