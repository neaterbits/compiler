package dev.nimbler.compiler.parser.recursive.cached.annotations.elements;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.parser.recursive.cached.ScratchList;
import dev.nimbler.compiler.parser.recursive.cached.annotations.CachedAnnotationsList;
import dev.nimbler.compiler.parser.recursive.cached.expressions.ContextWriter;
import dev.nimbler.compiler.parser.recursive.cached.expressions.LanguageOperatorPrecedence;

public interface CachedAnnotationElementsList extends ScratchList<CachedAnnotationElements> {

    CachedAnnotationElement addExpression(int startContext, long name, int nameContext, ContextWriter contextWriter, LanguageOperatorPrecedence languageOperatorPrecedence);
    
    void addAnnotation(int startContext, long name, int nameContext, CachedAnnotationsList annotationsList);
    
    void addAnnotationElement(int startContext, long name, int nameContext, Context endContext);

    void addValueList(int startContext, long name, int nameContext, CachedAnnotationElementsList annotationElementsList);
}
