package dev.nimbler.ide.component.common.instantiation;

import java.util.Objects;

public abstract class Named {
	private final String name;
	private final String displayName;
	
	
	public Named(String name, String displayName) {
		
		Objects.requireNonNull(name);
		
		this.name = name;
		this.displayName = displayName;
	}
	
	protected Named(Named named) {
		this.name = named.name;
		this.displayName = named.displayName;
	}
	
	
	public final String getName() {
		return name;
	}

	public final String getDisplayName() {
		return displayName != null ? displayName : name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Named other = (Named) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name;
	}
}
