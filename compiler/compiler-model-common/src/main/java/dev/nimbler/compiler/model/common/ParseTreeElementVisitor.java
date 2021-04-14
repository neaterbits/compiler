package dev.nimbler.compiler.model.common;

import dev.nimbler.compiler.types.ParseTreeElement;

public interface ParseTreeElementVisitor {

    void onElement(int parseTreeElementRef, ParseTreeElement type);
    
}
