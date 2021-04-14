package dev.nimbler.compiler.ast.objects.type;

public abstract class BaseType {
	private final boolean nullable;

	public abstract <T, R> R visit(TypeVisitor<T, R> visitor, T param);

	protected BaseType(boolean nullable) {
		this.nullable = nullable;
	}

	public final boolean isNullable() {
		return nullable;
	}

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (nullable ? 1231 : 1237);
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
		final BaseType other = (BaseType) obj;
		if (nullable != other.nullable)
			return false;
		return true;
	}
}
