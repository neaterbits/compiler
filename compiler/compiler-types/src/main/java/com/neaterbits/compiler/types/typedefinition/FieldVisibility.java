package com.neaterbits.compiler.types.typedefinition;

import java.util.Objects;

import com.neaterbits.language.common.types.Visibility;

public class FieldVisibility implements FieldModifier {
	
    public static final FieldVisibility PUBLIC = new FieldVisibility(Visibility.PUBLIC); 
    public static final FieldVisibility PRIVATE = new FieldVisibility(Visibility.PRIVATE);
    public static final FieldVisibility NAMESPACE = new FieldVisibility(Visibility.NAMESPACE);
    public static final FieldVisibility NAMESPACE_AND_SUBCLASSES = new FieldVisibility(Visibility.NAMESPACE_AND_SUBCLASSES);
    
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
