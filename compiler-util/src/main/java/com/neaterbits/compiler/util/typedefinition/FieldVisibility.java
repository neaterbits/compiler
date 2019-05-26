package com.neaterbits.compiler.util.typedefinition;

import java.util.Objects;

import com.neaterbits.compiler.util.model.Visibility;

public class FieldVisibility implements FieldModifier {
	
	private final Visibility visibility;
	
	public FieldVisibility(Visibility visibility) {

		Objects.requireNonNull(visibility);
		
		this.visibility = visibility;
	}

	public Visibility getVisibility() {
		return visibility;
	}

	@Override
	public <T, R> R visit(FieldModifierVisitor<T, R> visitor, T param) {
		return visitor.onFieldVisibility(this, param);
	}
}
