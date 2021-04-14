package dev.nimbler.compiler.model.common;

public interface TypeVisitor extends NamespaceVisitor {

    void onClassStart(CharSequence name);

    void onClassEnd();

    void onInterfaceStart(CharSequence name);

    void onInterfaceEnd();

    void onEnumStart(CharSequence name);

    void onEnumEnd();
}
