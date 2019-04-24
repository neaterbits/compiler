package com.neaterbits.compiler.ast.typereference;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;

public final class ResolveLaterTypeReference extends TypeReference {

	private final ScopedName scopedName;

	public ResolveLaterTypeReference(Context context, ScopedName typeName) {
		super(context);

		this.scopedName = typeName;
	}

	public ScopedName getScopedName() {
		return scopedName;
	}
	
	@Override
	public TypeName getTypeName() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getDebugName() {
		return scopedName.getName();
	}

	@Override
	public String toString() {
		return "ResolveLaterTypeReference [scopedName=" + scopedName + "]";
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onResolveLaterTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
