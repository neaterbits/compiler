package com.neaterbits.compiler.common;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.type.primitive.BuiltinType;

public final class BuiltinTypeReference extends ResolvedTypeReference {

	private final BuiltinType type;

	public BuiltinTypeReference(Context context, BuiltinType type) {
		super(context);

		Objects.requireNonNull(type);
		
		this.type = type;
	}

	@Override
	public BaseType getType() {
		return type;
	}

	@Override
	public String toString() {
		return "ResolvedTypeReference [type=" + type + "]";
	}

	@Override
	public <T, R> R visit(TypeReferenceVisitor<T, R> visitor, T param) {
		return visitor.onBuiltinTypeReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuiltinTypeReference other = (BuiltinTypeReference) obj;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
}
