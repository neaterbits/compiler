package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.StackEntry;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.AnnotationSetter;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.TypeReferenceSetter;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.VariableModifierSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

public abstract class BaseStackVariableDeclaration<ANNOTATION, VARIABLE_MODIFIER_HOLDER, TYPE_REFERENCE> extends StackEntry
	implements
	    VariableModifierSetter<VARIABLE_MODIFIER_HOLDER>,
	    TypeReferenceSetter<TYPE_REFERENCE>,
	    VariableNameSetter,
	    AnnotationSetter<ANNOTATION> {
	    
    private final List<ANNOTATION> annotations;
	private final List<VARIABLE_MODIFIER_HOLDER> modifiers;
	private TYPE_REFERENCE typeReference;
	private String name;
	private Context nameContext;
	private int numDims;

	BaseStackVariableDeclaration(ParseLogger parseLogger) {
		super(parseLogger);
	
		this.annotations = new ArrayList<>();
		this.modifiers = new ArrayList<>();
	}
	
	public final List<ANNOTATION> getAnnotations() {
        return annotations;
    }

    @Override
    public void addAnnotation(ANNOTATION annotation) {

        Objects.requireNonNull(annotation);
        
        annotations.add(annotation);
    }

    @Override
	public final void addModifier(VARIABLE_MODIFIER_HOLDER modifier) {
		Objects.requireNonNull(modifier);
		
		modifiers.add(modifier);
	}

	public final List<VARIABLE_MODIFIER_HOLDER> getModifiers() {
		return modifiers;
	}

	@Override
	public final void setTypeReference(TYPE_REFERENCE typeReference) {
		Objects.requireNonNull(typeReference);
		
		if (this.typeReference != null) {
			throw new IllegalStateException("typeReference already set: " + typeReference);
		}

		this.typeReference = typeReference;
	}

	@Override
	public final void init(String name, Context nameContext, int numDims) {
		Objects.requireNonNull(name);

		if (this.name != null) {
			throw new IllegalStateException("Name already set");
		}
		
		this.name = name;
		this.nameContext = nameContext;
		this.numDims = numDims;
	}
	
	public final String getName() {
		return name;
	}
	
	public final Context getNameContext() {
		return nameContext;
	}

	public final int getNumDims() {
		return numDims;
	}

	public final TYPE_REFERENCE getTypeReference() {
		return typeReference;
	}
}
