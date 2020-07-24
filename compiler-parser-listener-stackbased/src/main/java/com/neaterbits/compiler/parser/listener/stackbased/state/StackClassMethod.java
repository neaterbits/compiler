package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.setters.AnnotationSetter;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackClassMethod<STATEMENT, PARAMETER, TYPE_REFERENCE, ANNOTATION, MODIFIER_HOLDER>
	extends CallableStackEntry<STATEMENT, PARAMETER, TYPE_REFERENCE>
	implements AnnotationSetter<ANNOTATION> {

    private final List<ANNOTATION> annotations;
	private final List<MODIFIER_HOLDER> modifiers;
	
	public StackClassMethod(ParseLogger parseLogger) {
		super(parseLogger);
		
		this.annotations = new ArrayList<>();
		this.modifiers = new ArrayList<>();
	}

	public StackClassMethod(ParseLogger parseLogger, String name, Context nameContext) {
		super(parseLogger, name, nameContext);

		this.annotations = new ArrayList<>();
		this.modifiers = new ArrayList<>();
	}

	@Override
	public void addAnnotation(ANNOTATION annotation) {
        Objects.requireNonNull(annotation);

        annotations.add(annotation);
    }

    public List<ANNOTATION> getAnnotations() {
        return annotations;
    }

	public void addModifier(MODIFIER_HOLDER modifier) {
		Objects.requireNonNull(modifier);

		modifiers.add(modifier);
	}

	public List<MODIFIER_HOLDER> getModifiers() {
		return modifiers;
	}
}
