package dev.nimbler.compiler.model.common;

import dev.nimbler.language.common.types.ScopedName;

public interface TypeReferenceVisitor<COMPILATION_UNIT> extends NamespaceVisitor {

    void onNonScopedTypeReference(COMPILATION_UNIT compilationUnit, int parseTreeElement, CharSequence name);

    void onScopedTypeReference(COMPILATION_UNIT compilationUnit, int parseTreeElement, ScopedName scopeName);

    void onResolvedTypeReference(COMPILATION_UNIT compilationUnit, int parseTreeElement, int typeNo);
}
