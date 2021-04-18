package dev.nimbler.compiler.parser.listener.stackbased.state.setters;

import org.jutils.parse.context.Context;

public interface InterfaceSetter<TYPE_REFERENCE> {

    void addImplementedInterface(String implementsKeyword, Context implementsKeywordContext, TYPE_REFERENCE implementedInterface);
}
