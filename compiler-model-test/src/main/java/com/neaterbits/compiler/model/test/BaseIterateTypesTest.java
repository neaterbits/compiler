package com.neaterbits.compiler.model.test;

import org.junit.Test;
import org.mockito.Mockito;

import com.neaterbits.compiler.model.common.ParseTreeModel;
import com.neaterbits.compiler.model.common.TypeVisitor;
import com.neaterbits.compiler.parser.listener.common.ParseTreeListener;

public abstract class BaseIterateTypesTest<COMPILATION_UNIT> {

    protected abstract ParseTreeListener<COMPILATION_UNIT>
        makeParseTreeListener(String fileName, TestTokenizer testTokenizer);

    protected abstract ParseTreeModel<COMPILATION_UNIT> makeParseTreeModel();

    @Test
    public void testIterateTypes() {

        final TestTokenizer testTokenizer = new TestTokenizer();

        final ParseTreeListener<COMPILATION_UNIT> parserListener
            = makeParseTreeListener("file", testTokenizer);

        final ParseTreeBuilder<COMPILATION_UNIT> builder
            = new ParseTreeBuilder<>(parserListener, testTokenizer);

        builder.startCompilationUnit();

        builder.startNamespace();

        builder.addNamespacePart("namespace");

        builder.startClass("TestClass");

        builder.endClass();

        builder.startInterface("TestInterface");

        builder.endInterface();

        builder.startEnum("TestEnum");

        builder.endEnum();

        builder.endNamespace();

        final COMPILATION_UNIT compilationUnit = builder.endCompilationUnit();

        final ParseTreeModel<COMPILATION_UNIT> parseTreeModel = makeParseTreeModel();

        final TypeVisitor typeVisitor = Mockito.mock(TypeVisitor.class);
        Mockito.inOrder(typeVisitor);

        parseTreeModel.iterateTypes(compilationUnit, typeVisitor);

        Mockito.verify(typeVisitor).onNamespaceStart();

        Mockito.verify(typeVisitor).onNamespacePart("namespace");

        Mockito.verify(typeVisitor).onClassStart("TestClass");

        Mockito.verify(typeVisitor).onClassEnd();

        Mockito.verify(typeVisitor).onInterfaceStart("TestInterface");

        Mockito.verify(typeVisitor).onInterfaceEnd();

        Mockito.verify(typeVisitor).onEnumStart("TestEnum");

        Mockito.verify(typeVisitor).onEnumEnd();

        Mockito.verify(typeVisitor).onNamespaceEnd();

        Mockito.verifyNoMoreInteractions(typeVisitor);
    }
}
