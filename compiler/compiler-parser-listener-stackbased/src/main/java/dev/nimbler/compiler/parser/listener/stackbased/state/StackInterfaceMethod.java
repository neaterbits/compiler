package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackInterfaceMethod<
		STATEMENT,
		PARAMETER,
		TYPE_REFERENCE,
		ANNOTATION,
		INTERFACE_METHOD_MODIFIER_HOLDER>

	extends CallableStackEntry<STATEMENT, PARAMETER, TYPE_REFERENCE> {

    private final List<ANNOTATION> annotations;
	private final List<INTERFACE_METHOD_MODIFIER_HOLDER> modifiers;

	public StackInterfaceMethod(ParseLogger parseLogger, String name, Context nameContext) {
		super(parseLogger, name, nameContext);

		this.annotations = new ArrayList<>();
		this.modifiers = new ArrayList<>();
	}

	public StackInterfaceMethod(ParseLogger parseLogger) {
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

    public void addModifier(INTERFACE_METHOD_MODIFIER_HOLDER modifier) {
		Objects.requireNonNull(modifier);
		
		modifiers.add(modifier);
	}

	public List<INTERFACE_METHOD_MODIFIER_HOLDER> getModifiers() {
		return modifiers;
	}
}
