package com.neaterbits.compiler.model.common;

import com.neaterbits.compiler.types.ParseTreeElement;

public interface ParseTreeElementVisitor {

    void onElement(int parseTreeElementRef, ParseTreeElement type);
    
}
