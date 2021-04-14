package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.parser.listener.stackbased.state.setters.AnnotationSetter;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.NamedGenericParametersSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackClassMethod<STATEMENT, PARAMETER, TYPE_REFERENCE, ANNOTATION, MODIFIER_HOLDER, NAMED_GENERIC_TYPE_PARAMETERS>
	extends CallableStackEntry<STATEMENT, PARAMETER, TYPE_REFERENCE>
	implements AnnotationSetter<ANNOTATION>, NamedGenericParametersSetter<NAMED_GENERIC_TYPE_PARAMETERS> {

    private final List<ANNOTATION> annotations;
	private final List<MODIFIER_HOLDER> modifiers;
	
	private List<NAMED_GENERIC_TYPE_PARAMETERS> genericTypes;

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

    @Override
    public void setGenericTypes(List<NAMED_GENERIC_TYPE_PARAMETERS> genericTypes) {

        Objects.requireNonNull(genericTypes);
        
        if (this.genericTypes != null) {
            throw new IllegalStateException();
        }
     
        this.genericTypes = genericTypes;
    }

    public List<NAMED_GENERIC_TYPE_PARAMETERS> getGenericTypes() {
        return genericTypes;
    }
}
