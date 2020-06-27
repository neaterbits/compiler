package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.StackEntry;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.AnnotationElementSetter;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.PrimarySetter;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackAnnotationElement<NAME, PRIMARY, ANNOTATION, ANNOTATION_ELEMENT> extends StackEntry
            implements PrimarySetter<PRIMARY>, AnnotationElementSetter<ANNOTATION_ELEMENT> {

    private final NAME name;
    
    private PRIMARY expression;
    private ANNOTATION annotation;
    private List<ANNOTATION_ELEMENT> elements;
    
    public StackAnnotationElement(ParseLogger parseLogger, NAME name) {
        super(parseLogger);
        
        this.name = name;
    }

    @Override
    public void addPrimary(PRIMARY expression) {
        
        Objects.requireNonNull(expression);
        
        if (this.expression != null) {
            throw new IllegalStateException();
        }
        
        this.expression = expression;
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

    public PRIMARY getExpression() {
        return expression;
    }

    public ANNOTATION getAnnotation() {
        return annotation;
    }

    public List<ANNOTATION_ELEMENT> getElements() {
        return elements;
    }
}
