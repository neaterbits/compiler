package dev.nimbler.compiler.model.common;

import dev.nimbler.compiler.types.ParseTreeElement;

public interface ElementVisitor<COMPILATION_UNIT> {

    void onElementStart(COMPILATION_UNIT compilationUnit, int parseTreeRef, ParseTreeElement elementType);

    void onElementEnd(COMPILATION_UNIT compilationUnit, int parseTreeRef, ParseTreeElement elementType);
}
