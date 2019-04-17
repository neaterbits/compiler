package com.neaterbits.compiler.ast.typereference;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;

public final class ResolveLaterTypeReference extends TypeReference {

	private final ScopedName typeName;
	private BaseType resolved;

	public ResolveLaterTypeReference(Context context, ScopedName typeName) {
		super(context);

		this.typeName = typeName;
	}

	public ScopedName getScopedName() {
		return typeName;
	}
	
	@Override
	public TypeName getTypeName() {
		throw new UnsupportedOperationException();
	}

	public void setResolved(BaseType resolved) {
		this.resolved = resolved;
	}

	@Override
	public String getDebugName() {
		return typeName.getName();
	}

	@Override
	public BaseType getType() {
		
		if (resolved == null) {
			throw new IllegalStateException("Not yet resolved: " + typeName);
		}

		return resolved;
	}

	@Override
	public String toString() {
		return "ResolveLaterTypeReference [typeName=" + typeName + ", resolved=" + resolved + "]";
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onResolveLaterTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
