package com.neaterbits.compiler.model.common;

public interface UnresolvedScopesListener extends ScopesListener {

    void onUnresolvedNamePrimary(int namePrimaryParseTreeRef, String name);

}
