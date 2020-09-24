package com.neaterbits.compiler.model.encoded;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collections;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.neaterbits.compiler.ast.encoded.EncodedCompilationUnit;
import com.neaterbits.compiler.model.common.TypeVisitor;

public class IterateTypesTest {

    @Test
    public void testIterateTypes() {

        final TestTokenizer testTokenizer = new TestTokenizer();

        final EncodedParserListener parserListener = new EncodedParserListener("file", testTokenizer);

        final ParseTreeBuilder<EncodedCompilationUnit> builder
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

        final EncodedCompilationUnit compilationUnit = builder.endCompilationUnit();

        final EncodedProgramModel programModel = new EncodedProgramModel(Collections.emptyList());

        final TypeVisitor typeVisitor = Mockito.mock(TypeVisitor.class);

        programModel.iterateTypes(compilationUnit, typeVisitor);

        final ArgumentCaptor<Long> stringCaptor = ArgumentCaptor.forClass(Long.class);
        Mockito.verify(typeVisitor).onNamespaceStart();

        Mockito.verify(typeVisitor).onNamespacePart(stringCaptor.capture());
        assertThat(compilationUnit.getStringFromRef(stringCaptor.getValue().intValue())).isEqualTo("namespace");

        Mockito.verify(typeVisitor).onClassStart(stringCaptor.capture());
        assertThat(compilationUnit.getStringFromRef(stringCaptor.getValue().intValue())).isEqualTo("TestClass");

        Mockito.verify(typeVisitor).onClassEnd();

        Mockito.verify(typeVisitor).onInterfaceStart(stringCaptor.capture());
        assertThat(compilationUnit.getStringFromRef(stringCaptor.getValue().intValue())).isEqualTo("TestInterface");

        Mockito.verify(typeVisitor).onInterfaceEnd();

        Mockito.verify(typeVisitor).onEnumStart(stringCaptor.capture());
        assertThat(compilationUnit.getStringFromRef(stringCaptor.getValue().intValue())).isEqualTo("TestEnum");

        Mockito.verify(typeVisitor).onEnumEnd();

        Mockito.verify(typeVisitor).onNamespaceEnd();

        Mockito.verifyNoMoreInteractions(typeVisitor);
    }
}
