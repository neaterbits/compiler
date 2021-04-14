package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.StackEntry;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.AnnotationElementSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.AnnotationSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.PrimarySetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.VariableReferenceSetter;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackAnnotationElement<
            NAME,
            EXPRESSION,
            PRIMARY extends EXPRESSION,
            VARIABLE_REFERENCE extends EXPRESSION,
            ANNOTATION,
            ANNOTATION_ELEMENT> extends StackEntry
        implements 
                PrimarySetter<PRIMARY>,
                VariableReferenceSetter<VARIABLE_REFERENCE>,
                AnnotationElementSetter<ANNOTATION_ELEMENT>,
                AnnotationSetter<ANNOTATION> {

    private final NAME name;
    
    private EXPRESSION expression;
    private ANNOTATION annotation;
    private List<ANNOTATION_ELEMENT> elements;
    
    public StackAnnotationElement(ParseLogger parseLogger, NAME name) {
        super(parseLogger);
        
        this.name = name;
    }

    @Override
    public void addPrimary(PRIMARY primary) {
        
        Objects.requireNonNull(primary);
        
        if (this.expression != null) {
            throw new IllegalStateException();
        }
        
        this.expression = primary;
    }

    @Override
    public void setVariableReference(VARIABLE_REFERENCE variableReference) {

        Objects.requireNonNull(variableReference);
        
        if (this.expression != null) {
            throw new IllegalStateException();
        }
        
        this.expression = variableReference;
    }

    @Override
    public void addAnnotation(ANNOTATION annotation) {
        
        Objects.requireNonNull(annotation);
        
        if (this.annotation != null) {
            throw new IllegalStateException();
        }
        
        this.annotation = annotation;
    }

    @Override
    public void addAnnotationElement(ANNOTATION_ELEMENT element) {

        Objects.requireNonNull(element);
        
        if (elements == null) {
            this.elements = new ArrayList<>();
        }
        
        elements.add(element);
    }

    public NAME getName() {
        return name;
    }

    public EXPRESSION getExpression() {
        return expression;
    }

    public ANNOTATION getAnnotation() {
        return annotation;
    }

    public List<ANNOTATION_ELEMENT> getElements() {
        return elements;
    }
}
