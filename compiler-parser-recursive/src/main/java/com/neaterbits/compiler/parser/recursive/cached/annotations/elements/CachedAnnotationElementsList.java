package com.neaterbits.compiler.parser.recursive.cached.annotations.elements;

import com.neaterbits.compiler.parser.recursive.cached.ScratchList;
import com.neaterbits.compiler.parser.recursive.cached.annotations.CachedAnnotationsList;
import com.neaterbits.compiler.parser.recursive.cached.expressions.ContextWriter;
import com.neaterbits.compiler.parser.recursive.cached.expressions.LanguageOperatorPrecedence;
import com.neaterbits.compiler.util.Context;

public interface CachedAnnotationElementsList extends ScratchList<CachedAnnotationElements> {

    CachedAnnotationElement addExpression(int startContext, long name, int nameContext, ContextWriter contextWriter, LanguageOperatorPrecedence languageOperatorPrecedence);
    
    void addAnnotation(int startContext, long name, int nameContext, CachedAnnotationsList annotationsList);
    
    void addAnnotationElement(int startContext, long name, int nameContext, Context endContext);

    void addValueList(int startContext, long name, int nameContext, CachedAnnotationElementsList annotationElementsList);
}
