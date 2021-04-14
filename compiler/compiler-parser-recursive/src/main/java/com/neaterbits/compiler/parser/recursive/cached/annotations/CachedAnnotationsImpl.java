package com.neaterbits.compiler.parser.recursive.cached.annotations;

import com.neaterbits.compiler.parser.recursive.cached.ScratchBuf;
import com.neaterbits.compiler.parser.recursive.cached.ScratchEntity;
import com.neaterbits.compiler.parser.recursive.cached.annotations.elements.CachedAnnotationElementsList;
import com.neaterbits.compiler.parser.recursive.cached.names.NamesList;
import com.neaterbits.util.parse.context.Context;

public final class CachedAnnotationsImpl
    extends ScratchEntity<CachedAnnotation, CachedAnnotations, CachedAnnotationsList>
    implements CachedAnnotations, CachedAnnotationsList {

    public CachedAnnotationsImpl(ScratchBuf<CachedAnnotation, CachedAnnotations, CachedAnnotationsList, ?> buf) {
        super(buf);
    }

    @Override
    protected CachedAnnotation createPart() {
        return new CachedAnnotation();
    }

    @Override
    protected CachedAnnotations getToProcess() {
        return this;
    }

    @Override
    protected CachedAnnotationsList getList() {
        return this;
    }

    @Override
    public CachedAnnotation getAnnotation(int index) {
        
        return get(index);
    }

    @Override
    public int count() {
        
        return getCount();
    }

    @Override
    public void addAnnotation(int startContext, NamesList typeName, CachedAnnotationElementsList elements, Context endContext) {

        getOrCreate().init(startContext, typeName, elements, endContext);
    }
}
