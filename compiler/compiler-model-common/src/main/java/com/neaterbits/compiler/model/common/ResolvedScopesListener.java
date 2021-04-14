package com.neaterbits.compiler.model.common;

public interface ResolvedScopesListener extends ScopesListener {

    <T> void onPrimaryListNameReference(T primaryList, int nameParseTreeRef, String name);

    <T> void onNonPrimaryListNameReference(int nameParseTreeRef, String name);

    <T> T onArrayAccessReference(T primaryList, int arrayAccessParseTreeRef);
    
    <T> T onFieldAccessReference(T primaryList, int fieldAccessParseTreeRef);

    <T> T onStaticMemberReference(T primaryList, int staticMemberParseTreeRef);

}
