package com.neaterbits.compiler.parser.recursive.cached.annotations.elements;

public interface CachedAnnotationElements {

    CachedAnnotationElement getAnnotationElement(int index);
    
    int count();
}
