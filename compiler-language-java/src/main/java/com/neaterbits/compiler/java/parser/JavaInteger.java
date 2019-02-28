package com.neaterbits.compiler.java.parser;

import java.util.Objects;

import com.neaterbits.compiler.common.ast.expression.Base;

final class JavaInteger {
	
	private final long value;
	private final Base base;
	private final int bits;
	
	public JavaInteger(long value, Base base, int bits) {

		Objects.requireNonNull(base);

		this.value = value;
		this.base = base;
		this.bits = bits;
	}

	public long getValue() {
		return value;
	}

	public Base getBase() {
		return base;
	}

	public int getBits() {
		return bits;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((base == null) ? 0 : base.hashCode());
		result = prime * result + bits;
		result = prime * result + (int) (value ^ (value >>> 32));
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
		JavaInteger other = (JavaInteger) obj;
		if (base != other.base)
			return false;
		if (bits != other.bits)
			return false;
		if (value != other.value)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "JavaInteger [value=" + value + ", base=" + base + ", bits=" + bits + "]";
	}
}
