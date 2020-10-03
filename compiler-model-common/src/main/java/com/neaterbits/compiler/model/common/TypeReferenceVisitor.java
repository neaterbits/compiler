package com.neaterbits.compiler.model.common;

public interface TypeReferenceVisitor {

    void onNonScopedTypeReference(int parseTreeElement, CharSequence name);

    void onResolvedTypeReference(int parseTreeElement, int typeNo);
}
