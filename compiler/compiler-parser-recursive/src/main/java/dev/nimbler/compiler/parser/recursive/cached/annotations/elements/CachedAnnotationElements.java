package dev.nimbler.compiler.parser.recursive.cached.annotations.elements;

public interface CachedAnnotationElements {

    CachedAnnotationElement getAnnotationElement(int index);
    
    int count();
}
