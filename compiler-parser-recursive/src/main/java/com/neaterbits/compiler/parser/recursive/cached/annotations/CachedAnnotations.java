package com.neaterbits.compiler.parser.recursive.cached.annotations;

public interface CachedAnnotations {

    CachedAnnotation getAnnotation(int index);
    
    int count();
}
