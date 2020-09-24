package com.neaterbits.compiler.model.common;

public interface TypeVisitor {

    void onNamespaceStart();

    void onNamespacePart(CharSequence part);

    void onNamespaceEnd();

    void onClassStart(CharSequence name);

    void onClassEnd();

    void onInterfaceStart(CharSequence name);

    void onInterfaceEnd();

    void onEnumStart(CharSequence name);

    void onEnumEnd();
}
