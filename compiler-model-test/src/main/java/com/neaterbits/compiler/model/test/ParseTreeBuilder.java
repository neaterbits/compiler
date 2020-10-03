package com.neaterbits.compiler.model.test;

import java.util.Objects;

import com.neaterbits.compiler.parser.listener.common.ParseTreeListener;
import com.neaterbits.compiler.types.ReferenceType;
import com.neaterbits.compiler.util.Context;

public final class ParseTreeBuilder<COMPILATION_UNIT> {

    private final ParseTreeListener<COMPILATION_UNIT> listener;
    private final TestTokenizer testTokenizer;

    private int context;

    ParseTreeBuilder(ParseTreeListener<COMPILATION_UNIT> listener, TestTokenizer testTokenizer) {

        Objects.requireNonNull(listener);
        Objects.requireNonNull(testTokenizer);

        this.listener = listener;
        this.testTokenizer = testTokenizer;
    }

    private int addStartContext() {
        return context ++;
    }

    private int addTokenContext() {
        return context ++;
    }

    private int getStartContext() {
        return -1;
    }

    private Context getEndContext() {
        return null;
    }

    public final void startCompilationUnit() {

        listener.onCompilationUnitStart(addStartContext());
    }

    public final COMPILATION_UNIT endCompilationUnit() {

        return listener.onCompilationUnitEnd(getStartContext(), getEndContext());
    }

    public final void startNamespace() {

        listener.onNamespaceStart(
                getStartContext(),
                testTokenizer.addString("namespace"),
                addTokenContext());
    }

    public final void addNamespacePart(String part) {

        listener.onNamespacePart(addStartContext(), testTokenizer.addString(part));
    }

    public final void endNamespace() {

        listener.onNameSpaceEnd(getStartContext(), getEndContext());
    }

    public final void startClass(String className) {

        listener.onTypeDefinitionStart(addStartContext());

        listener.onClassStart(
                addStartContext(),
                testTokenizer.addString("class"),
                addTokenContext(),
                testTokenizer.addString(className),
                addTokenContext());
    }

    public final void endClass() {

        listener.onClassEnd(getStartContext(), getEndContext());

        listener.onTypeDefinitionEnd(getStartContext(), getEndContext());
    }

    public final void startInterface(String interfaceName) {

        listener.onInterfaceStart(
                addStartContext(),
                testTokenizer.addString("interface"),
                addTokenContext(),
                testTokenizer.addString(interfaceName),
                addTokenContext());
    }

    public final void endInterface() {

        listener.onInterfaceEnd(getStartContext(), getEndContext());
    }

    public final void startEnum(String enumName) {

        listener.onEnumStart(
                addStartContext(),
                testTokenizer.addString("enum"),
                addTokenContext(),
                testTokenizer.addString(enumName),
                addTokenContext());
    }

    public final void endEnum() {

        listener.onEnumEnd(getStartContext(), getEndContext());
    }

    public void addNonScopedTypeReference(String name) {

        listener.onNonScopedTypeReferenceStart(
                addStartContext(),
                testTokenizer.addString(name),
                ReferenceType.REFERENCE);

        listener.onNonScopedTypeReferenceEnd(getStartContext(), getEndContext());
    }
}
