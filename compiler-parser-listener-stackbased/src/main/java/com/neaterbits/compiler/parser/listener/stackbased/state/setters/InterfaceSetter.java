package com.neaterbits.compiler.parser.listener.stackbased.state.setters;

import com.neaterbits.compiler.util.Context;

public interface InterfaceSetter<TYPE_REFERENCE> {

    void addImplementedInterface(String implementsKeyword, Context implementsKeywordContext, TYPE_REFERENCE implementedInterface);
}