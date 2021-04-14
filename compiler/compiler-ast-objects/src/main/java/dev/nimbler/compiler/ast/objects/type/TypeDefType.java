package dev.nimbler.compiler.ast.objects.type;

public final class TypeDefType extends ResolvableType {

	private final BaseType delegate;
	
	public TypeDefType(CompleteName completeName, BaseType delegate) {
		super(completeName, delegate.isNullable());

		this.delegate = delegate;
	}

	public BaseType getDelegate() {
		return delegate;
	}

	@Override
	public <T, R> R visit(TypeVisitor<T, R> visitor, T param) {
		return visitor.onTypeDef(this, param);
	}
}
