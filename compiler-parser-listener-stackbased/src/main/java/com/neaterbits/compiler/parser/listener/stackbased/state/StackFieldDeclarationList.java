package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackFieldDeclarationList<TYPE_REFERENCE, EXPRESSION, ANNOTATION, FIELD_MODIFIER_HOLDER>
		extends BaseStackVariableDeclarationList<TYPE_REFERENCE, EXPRESSION> {

    private final List<ANNOTATION> annotations;
	private final List<FIELD_MODIFIER_HOLDER> modifiers;

	public StackFieldDeclarationList(ParseLogger parseLogger) {
		super(parseLogger);

		this.annotations = new ArrayList<>();
		this.modifiers = new ArrayList<>();
	}

    public void addAnnotation(ANNOTATION annotation) {
        Objects.requireNonNull(annotation);
        
        annotations.add(annotation);
    }

    public List<ANNOTATION> getAnnotations() {
        return annotations;
    }
	
	public void addFieldModifier(FIELD_MODIFIER_HOLDER modifier) {
		Objects.requireNonNull(modifier);
		
		modifiers.add(modifier);
	}

	public List<FIELD_MODIFIER_HOLDER> getModifiers() {
		return modifiers;
	}
}
