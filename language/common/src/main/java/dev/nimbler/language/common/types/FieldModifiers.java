package dev.nimbler.language.common.types;

import java.util.Objects;

public class FieldModifiers {

	private final boolean isStatic;
	private final Visibility visibility;
	private final Mutability mutability;
	private final boolean isVolatile;
	private final boolean isTransient;
	
	public FieldModifiers(
			boolean isStatic,
			Visibility visibility,
			Mutability mutability,
			boolean isVolatile,
			boolean isTransient) {

		Objects.requireNonNull(visibility);
		Objects.requireNonNull(mutability);
		
		this.isStatic = isStatic;
		this.visibility = visibility;
		this.mutability = mutability;
		this.isVolatile = isVolatile;
		this.isTransient = isTransient;
	}

	public final boolean isStatic() {
		return isStatic;
	}

	public final Visibility getVisibility() {
		return visibility;
	}

	public final Mutability getMutability() {
		return mutability;
	}

	public final boolean isVolatile() {
		return isVolatile;
	}

	public final boolean isTransient() {
		return isTransient;
	}
}
