package dev.nimbler.compiler.resolver.passes.typefinder;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.neaterbits.util.parse.ParserException;

import dev.nimbler.compiler.model.common.ParseTreeModel;
import dev.nimbler.compiler.model.common.TypeVisitor;
import dev.nimbler.compiler.resolver.passes.ParsedFilesAndCodeMap;
import dev.nimbler.compiler.util.parse.ParsedFile;
import dev.nimbler.language.codemap.compiler.CompilerCodeMap;
import dev.nimbler.language.codemap.compiler.IntCompilerCodeMap;
import dev.nimbler.language.common.types.ScopedName;
import dev.nimbler.language.common.types.TypeName;

public class TypeFinderPassTest {

    @Test
    public void testTypeFinderPass() throws IOException, ParserException {

        final Object compilationUnit = Mockito.mock(Object.class);

        @SuppressWarnings("unchecked")
        final ParseTreeModel<Object> parseTreeModel = Mockito.mock(ParseTreeModel.class);
        final ParsedFile parsedFile = Mockito.mock(ParsedFile.class);

        final CompilerCodeMap codeMap = new IntCompilerCodeMap();

        final ParsedFilesAndCodeMap<ParsedFile> input
            = new ParsedFilesAndCodeMap<>(Arrays.asList(parsedFile), codeMap);

        final TypeFinderPass<ParsedFile, Object> typeFinderPass = new TypeFinderPass<>(parseTreeModel);

        Mockito.when(parsedFile.getCompilationUnit()).thenReturn(compilationUnit);

        final FoundTypeFiles<ParsedFile> found = typeFinderPass.execute(input);

        Mockito.verify(parsedFile).getCompilationUnit();

        final ArgumentCaptor<TypeVisitor> typeVisitorCaptor
            = ArgumentCaptor.forClass(TypeVisitor.class);

        Mockito.verify(parseTreeModel).iterateTypes(
                ArgumentMatchers.same(compilationUnit),
                typeVisitorCaptor.capture());

        final TypeVisitor typeVisitor = typeVisitorCaptor.getValue();

        typeVisitor.onNamespaceStart();

        typeVisitor.onNamespacePart("com");
        typeVisitor.onNamespacePart("test");

        typeVisitor.onClassStart("TestClass");
        typeVisitor.onClassStart("TestInnerClass");

        typeVisitor.onClassEnd();
        typeVisitor.onClassEnd();

        typeVisitor.onInterfaceStart("TestInterface");
        typeVisitor.onInterfaceEnd();

        typeVisitor.onEnumStart("TestEnum");
        typeVisitor.onEnumEnd();

        typeVisitor.onNamespaceEnd();

        final TypeName testClassTypeName = new TypeName(
                new String [] { "com", "test" },
                null,
                "TestClass");

        assertThat(codeMap.getTypeNoByTypeName(testClassTypeName)).isEqualTo(0);
        assertThat(found.lookupByScopedName(
                        ScopedName.makeScopedName(new String [] { "com", "test", "TestClass" })))
            .isEqualTo(testClassTypeName);

        final TypeName testInnerClassTypeName = new TypeName(
                new String [] { "com", "test" },
                new String [] { "TestClass" },
                "TestInnerClass");

        assertThat(codeMap.getTypeNoByTypeName(testInnerClassTypeName)).isEqualTo(1);
        assertThat(found.lookupByScopedName(
                        ScopedName.makeScopedName(new String [] { "com", "test", "TestClass", "TestInnerClass" })))
            .isEqualTo(testInnerClassTypeName);

        final TypeName testInterfaceTypeName = new TypeName(
                new String [] { "com", "test" },
                null,
                "TestInterface");

        assertThat(codeMap.getTypeNoByTypeName(testInterfaceTypeName)).isEqualTo(2);
        assertThat(found.lookupByScopedName(
                        ScopedName.makeScopedName(new String [] { "com", "test", "TestInterface" })))
            .isEqualTo(testInterfaceTypeName);

        final TypeName testEnumTypeName = new TypeName(
                new String [] { "com", "test" },
                null,
                "TestEnum");

        assertThat(codeMap.getTypeNoByTypeName(testEnumTypeName)).isEqualTo(3);
        assertThat(found.lookupByScopedName(
                        ScopedName.makeScopedName(new String [] { "com", "test", "TestEnum" })))
            .isEqualTo(testEnumTypeName);

        Mockito.verifyNoMoreInteractions(compilationUnit, parsedFile, parseTreeModel);
    }
}
