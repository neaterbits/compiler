package com.neaterbits.compiler.common;

import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ScopedName;
import com.neaterbits.compiler.common.ast.type.BaseType;

public final class ResolveLaterTypeReference extends TypeReference {

	private final ScopedName typeName;
	private BaseType resolved;

	public ResolveLaterTypeReference(Context context, ScopedName typeName) {
		super(context);

		this.typeName = typeName;
	}

	public ScopedName getTypeName() {
		return typeName;
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
