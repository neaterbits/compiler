package com.neaterbits.compiler.model.common;

import com.neaterbits.compiler.util.ScopedName;

public interface TypeReferenceVisitor<COMPILATION_UNIT> extends NamespaceVisitor {

    void onNonScopedTypeReference(COMPILATION_UNIT compilationUnit, int parseTreeElement, CharSequence name);

    void onScopedTypeReference(COMPILATION_UNIT compilationUnit, int parseTreeElement, ScopedName scopeName);

    void onResolvedTypeReference(COMPILATION_UNIT compilationUnit, int parseTreeElement, int typeNo);
}
