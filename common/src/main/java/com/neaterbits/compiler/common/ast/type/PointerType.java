package com.neaterbits.compiler.common.ast.type;

import com.neaterbits.compiler.common.TypeReference;

public final class PointerType extends BaseType {
	private final TypeReference delegate;
	private final int levels; // 1 is normal pointer, 2 is int ** etc 

	public PointerType(TypeReference delegate, int levels) {
		super(true); // pointers are always nullable

		this.delegate = delegate;
		this.levels = levels;
	}

	public TypeReference getDelegate() {
		return delegate;
	}

	public int getLevels() {
		return levels;
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onPointer(this, param);
	}
}
