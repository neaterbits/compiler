package com.neaterbits.compiler.parser.recursive.cached.annotations.elements;

import com.neaterbits.compiler.parser.recursive.cached.ScratchBuf;
import com.neaterbits.compiler.parser.recursive.cached.ScratchEntity;
import com.neaterbits.compiler.parser.recursive.cached.annotations.CachedAnnotationsList;
import com.neaterbits.compiler.parser.recursive.cached.expressions.ContextWriter;
import com.neaterbits.compiler.parser.recursive.cached.expressions.LanguageOperatorPrecedence;
import com.neaterbits.util.parse.context.Context;

public final class CachedAnnotationElementsImpl
    extends ScratchEntity<
        CachedAnnotationElement,
        CachedAnnotationElements,
        CachedAnnotationElementsList>

    implements CachedAnnotationElements, CachedAnnotationElementsList {

    public CachedAnnotationElementsImpl(
            ScratchBuf<CachedAnnotationElement, CachedAnnotationElements, CachedAnnotationElementsList, ?> buf) {
        super(buf);
    }

    @Override
    protected CachedAnnotationElement createPart() {
        return new CachedAnnotationElement();
    }

    @Override
    protected CachedAnnotationElements getToProcess() {
        return this;
    }

    @Override
    protected CachedAnnotationElementsList getList() {
        return this;
    }

    @Override
    public CachedAnnotationElement getAnnotationElement(int index) {

        return get(index);
    }

    @Override
    public int count() {

        return getCount();
    }

    @Override
    public CachedAnnotationElement addExpression(
            int startContext,
            long name, int nameContext,
            ContextWriter contextWriter,
            LanguageOperatorPrecedence languageOperatorPrecedence) {
        
        final CachedAnnotationElement element = getOrCreate();
        
        element.initExpression(startContext, name, nameContext, contextWriter, languageOperatorPrecedence);

        return element;
    }

    @Override
    public void addAnnotation(int startContext, long name, int nameContext, CachedAnnotationsList annotationsList) {

        final CachedAnnotationElement element = getOrCreate();
        
        element.initAnnotation(startContext, name, nameContext, annotationsList);
    }

    @Override
    public void addAnnotationElement(int startContext, long name, int nameContext, Context endContext) {

        getOrCreate().initNameOnly(startContext, name, nameContext, endContext);
    }

    @Override
    public void addValueList(int startContext, long name, int nameContext, CachedAnnotationElementsList annotationElementsList) {

        getOrCreate().initValueList(startContext, name, nameContext, annotationElementsList);
    }
}
