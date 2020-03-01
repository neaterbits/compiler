package com.neaterbits.compiler.ast.objects.type;

public final class PointerType extends BaseType {
	private final BaseType delegate;
	private final int levels; // 1 is normal pointer, 2 is int ** etc 

	public PointerType(BaseType delegate, int levels) {
		super(true); // pointers are always nullable

		this.delegate = delegate;
		this.levels = levels;
	}

	public BaseType getDelegate() {
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
