package com.neaterbits.compiler.ast.typereference;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.TypeName;

public final class BuiltinTypeReference extends ResolvedNamedTypeReference {

	private final boolean isScalar;
	
	public BuiltinTypeReference(Context context, TypeName type, boolean isScalar) {
		super(context, type);
		
		this.isScalar = isScalar;
	}
	
	public boolean isScalar() {
		return isScalar;
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onBuiltinTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
