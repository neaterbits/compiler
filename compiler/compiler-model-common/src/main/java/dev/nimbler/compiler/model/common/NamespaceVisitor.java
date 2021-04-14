package dev.nimbler.compiler.model.common;

public interface NamespaceVisitor {

    void onNamespaceStart();

    void onNamespacePart(CharSequence part);

    void onNamespaceEnd();
}
