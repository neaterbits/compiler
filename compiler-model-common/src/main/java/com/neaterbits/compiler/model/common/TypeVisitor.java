package com.neaterbits.compiler.model.common;

public interface TypeVisitor {

    void onNamespaceStart();

    void onNamespacePart(long part);

    void onNamespaceEnd();

    void onClassStart(long name);

    void onClassEnd();

    void onInterfaceStart(long name);

    void onInterfaceEnd();

    void onEnumStart(long name);

    void onEnumEnd();
}
