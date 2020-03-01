package com.neaterbits.compiler.ast.objects.type.primitive;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.type.BaseType;
import com.neaterbits.compiler.ast.objects.type.TypeVisitor;

public final class ArrayType extends BaseType {

	private final BaseType elementType;
	private final int numDims;

	public ArrayType(BaseType elementType, int numDims, boolean nullable) {
		super(nullable);
		
		Objects.requireNonNull(elementType);

		this.elementType = elementType;
		this.numDims = numDims;
	}

	public BaseType getElementType() {
		return elementType;
	}

	public int getNumDims() {
		return numDims;
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onArray(this, param);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((elementType == null) ? 0 : elementType.hashCode());
		result = prime * result + numDims;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ArrayType other = (ArrayType) obj;
		if (elementType == null) {
			if (other.elementType != null)
				return false;
		} else if (!elementType.equals(other.elementType))
			return false;
		if (numDims != other.numDims)
			return false;
		return true;
	}
}
