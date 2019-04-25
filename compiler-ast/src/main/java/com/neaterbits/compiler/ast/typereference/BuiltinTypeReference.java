package com.neaterbits.compiler.ast.typereference;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.util.Context;

public final class BuiltinTypeReference extends ResolvedNamedTypeReference {

	private final BuiltinType type;

	public BuiltinTypeReference(Context context, BuiltinType type) {
		super(context, type.getTypeName());

		Objects.requireNonNull(type);
		
		this.type = type;
	}

	public BuiltinType getBuiltinType() {
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
