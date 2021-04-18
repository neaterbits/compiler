package dev.nimbler.compiler.parser.recursive.cached.annotations;

import java.util.Objects;

import org.jutils.parse.context.Context;
import org.jutils.parse.context.MutableContext;

import dev.nimbler.compiler.parser.recursive.cached.annotations.elements.CachedAnnotationElementsList;
import dev.nimbler.compiler.parser.recursive.cached.names.NamesList;

public final class CachedAnnotation {

    private final MutableContext endContext;
    
    private int startContext;
    
    private NamesList typeName;
    
    private CachedAnnotationElementsList elements;
    
    CachedAnnotation() {
        this.endContext = new MutableContext();
    }

    public void init(int startContext, NamesList typeName, CachedAnnotationElementsList elements, Context endContext) {

        Objects.requireNonNull(typeName);
        
        this.startContext = startContext;
        this.typeName = typeName;
        this.elements = elements;
        
        this.endContext.init(endContext);
    }
    
    public int getStartContext() {
        return startContext;
    }

    public NamesList getTypeName() {
        return typeName;
    }

    public CachedAnnotationElementsList getElements() {
        return elements;
    }

    public Context getEndContext() {
        return endContext;
    }
}
