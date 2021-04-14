package com.neaterbits.compiler.util;

import com.neaterbits.build.types.TypeName;

public final class FieldType extends TypeName {

	private final boolean builtin;
	private final int sizeInBits;
	private final int arrayDimensions;
	
	public FieldType(String[] namespace, String[] outerTypes, String name, boolean builtin, int sizeInBits, int arrayDimensions) {
		super(namespace, outerTypes, name);

		this.builtin = builtin;
		this.sizeInBits = sizeInBits;
		this.arrayDimensions = arrayDimensions;
	}

	public boolean isBuiltinType() {
		return builtin;
	}

	public int getSizeInBits() {
		return sizeInBits;
	}

	public boolean isArrayType() {
		return arrayDimensions > 0;
	}

	public int getArrayDimensions() {
		return arrayDimensions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + arrayDimensions;
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
		FieldType other = (FieldType) obj;
		if (arrayDimensions != other.arrayDimensions)
			return false;
		return true;
	}
}
