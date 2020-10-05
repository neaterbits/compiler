package com.neaterbits.compiler.parser.recursive.cached.annotations;

import com.neaterbits.compiler.parser.recursive.cached.ScratchList;
import com.neaterbits.compiler.parser.recursive.cached.annotations.elements.CachedAnnotationElementsList;
import com.neaterbits.compiler.parser.recursive.cached.names.NamesList;
import com.neaterbits.util.parse.context.Context;

public interface CachedAnnotationsList extends ScratchList<CachedAnnotations> {

    void addAnnotation(int startContext, NamesList typeName, CachedAnnotationElementsList elements, Context endContext);

}
