package dev.nimbler.compiler.parser.recursive.cached.annotations.elements;

import org.jutils.parse.context.Context;
import org.jutils.parse.context.MutableContext;

import dev.nimbler.compiler.parser.recursive.cached.annotations.CachedAnnotationsList;
import dev.nimbler.compiler.parser.recursive.cached.expressions.ContextWriter;
import dev.nimbler.compiler.parser.recursive.cached.expressions.ExpressionCache;
import dev.nimbler.compiler.parser.recursive.cached.expressions.LanguageOperatorPrecedence;

public final class CachedAnnotationElement {

    public enum Type {
        NAME,
        ANNOTATION,
        EXPRESSION,
        VALUE_LIST
    }
    
    private final MutableContext endContext;
    
    private Type type;
    
    private int startContext;
    
    private long name;
    private int nameContext;
    
    // An expression
    private ExpressionCache expressions;
    
    // or an annotation
    private CachedAnnotationsList annotations;
    
    // or annotation elements
    private CachedAnnotationElementsList valueList;
    
    CachedAnnotationElement() {
        this.endContext = new MutableContext();
    }
    
    private void init(Type type, int startContext, long name, int nameContext) {
        
        this.type = type;
        this.startContext = startContext;
        this.name = name;
        this.nameContext = nameContext;
    }
    
    void initNameOnly(int startContext, long name, int nameContext, Context endContext) {

        init(Type.NAME, startContext, name, nameContext);
        
        this.name = name;
        this.nameContext = nameContext;
        
        this.endContext.init(endContext);
    }
    
    void initAnnotation(int startContext, long name, int nameContext, CachedAnnotationsList annotationsList) {
        
        init(Type.ANNOTATION, startContext, name, nameContext);
        
        this.annotations = annotationsList;
    }
    
    ExpressionCache initExpression(int startContext, long name, int nameContext, ContextWriter contextWriter, LanguageOperatorPrecedence languageOperatorPrecedence) {

        init(Type.EXPRESSION, startContext, name, nameContext);
        
        if (expressions == null) {
            this.expressions = new ExpressionCache(contextWriter, languageOperatorPrecedence);
        }
        
        return expressions;
    }

    void initValueList(int startContext, long name, int nameContext, CachedAnnotationElementsList annotationElementsList) {
        
        init(Type.VALUE_LIST, startContext, name, nameContext);
        
        this.valueList = annotationElementsList;
    }

    public Type getType() {
        
        return type;
    }
    
    public void initEndContext(Context endContext) {
        
        this.endContext.init(endContext);
    }

    public ExpressionCache getExpressionCache() {
        
        return expressions;
    }
    
    public CachedAnnotationsList getAnnotations() {
        return annotations;
    }
    
    public CachedAnnotationElementsList getValueList() {
        return valueList;
    }
    
    public int getStartContext() {
        return startContext;
    }

    public long getName() {
        return name;
    }

    public int getNameContext() {
        return nameContext;
    }

    public Context getEndContext() {
        return endContext;
    }
}
