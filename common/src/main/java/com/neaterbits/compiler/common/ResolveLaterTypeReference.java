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
	public BaseType getType() {
		
		if (resolved == null) {
			throw new IllegalStateException("Not yet resolved");
		}

		return resolved;
	}

	@Override
	public String toString() {
		return "ResolveLaterTypeReference [typeName=" + typeName + ", resolved=" + resolved + "]";
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
