package dev.nimbler.compiler.parser.recursive.cached.annotations;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.parser.recursive.cached.ScratchList;
import dev.nimbler.compiler.parser.recursive.cached.annotations.elements.CachedAnnotationElementsList;
import dev.nimbler.compiler.parser.recursive.cached.names.NamesList;

public interface CachedAnnotationsList extends ScratchList<CachedAnnotations> {

    void addAnnotation(int startContext, NamesList typeName, CachedAnnotationElementsList elements, Context endContext);

}
