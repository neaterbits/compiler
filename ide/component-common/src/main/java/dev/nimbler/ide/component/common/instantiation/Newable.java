package dev.nimbler.ide.component.common.instantiation;

import java.util.Objects;

public final class Newable extends Named {

	private final NewableType type;

	public Newable(NewableType type, String name, String displayName) {
		super(name, displayName);

		Objects.requireNonNull(type);
		
		this.type = type;
	}

	public NewableType getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Newable other = (Newable) obj;
		if (type != other.type)
			return false;
		return true;
	}
}
