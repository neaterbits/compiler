package dev.nimbler.compiler.resolver.passes.replacetyperefs;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.jutils.parse.ParserException;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import dev.nimbler.compiler.model.common.CompilationUnitModel;
import dev.nimbler.compiler.model.common.TypeReferenceVisitor;
import dev.nimbler.compiler.resolver.passes.ParsedModuleAndCodeMap;
import dev.nimbler.compiler.util.parse.ParsedFile;
import dev.nimbler.language.codemap.compiler.CompilerCodeMap;
import dev.nimbler.language.codemap.compiler.IntCompilerCodeMap;
import dev.nimbler.language.common.types.ScopedName;
import dev.nimbler.language.common.types.TypeName;

public class ReplaceTypeRefsPassTest {

    private ParsedFile parsedFile;
    private CompilationUnitModel<Object> compilationUnitModel;
    private Map<ScopedName, TypeName> typeNameByScopedName;
    private CompilerCodeMap codeMap;

    @Before
    public void init() {

        this.parsedFile = Mockito.mock(ParsedFile.class);

        @SuppressWarnings("unchecked")
        final CompilationUnitModel<Object> compilationUnitModel
            = Mockito.mock(CompilationUnitModel.class);

        this.compilationUnitModel = compilationUnitModel;

        this.typeNameByScopedName = new HashMap<>();

        this.codeMap = new IntCompilerCodeMap();
        
    }

    @Test
    public void testReplaceScopedTypeRef() throws IOException, ParserException {

        final ScopedName scopedName = ScopedName.makeScopedName(new String [] { "com", "scoped", "TestClass" });

        final TypeName typeName = new TypeName(
                new String [] { "com", "scoped" },
                null,
                "TestClass");

        final int typeNo = 321;

        final int parseTreeRef = 123;

        codeMap.addTypeMapping(typeName, typeNo);

        final Object compilationUnit = new Object();

        Mockito.when(parsedFile.getCompilationUnit())
            .thenReturn(compilationUnit);

        final ParsedModuleAndCodeMap<ParsedFile> parsedModuleAndCodeMap
            = new ParsedModuleAndCodeMap<>(Arrays.asList(parsedFile), -1, codeMap);

        final ReplaceTypeRefsPass<ParsedFile, Object> replaceTypeRefsPass
            = new ReplaceTypeRefsPass<>(compilationUnitModel);

        replaceTypeRefsPass.execute(parsedModuleAndCodeMap);

        Mockito.verify(parsedFile).getCompilationUnit();

        @SuppressWarnings("unchecked")
        final ArgumentCaptor<TypeReferenceVisitor<Object>> typeReferenceVisitorCaptor
            = ArgumentCaptor.forClass(TypeReferenceVisitor.class);

        Mockito.verify(compilationUnitModel).iterateTypeReferences(
                same(compilationUnit),
                typeReferenceVisitorCaptor.capture());

        final TypeReferenceVisitor<Object> typeReferenceVisitor = typeReferenceVisitorCaptor.getValue();

        typeReferenceVisitor.onNamespaceStart();
        typeReferenceVisitor.onNamespacePart("com");
        typeReferenceVisitor.onNamespacePart("test");

        typeReferenceVisitor.onScopedTypeReference(
                compilationUnit,
                parseTreeRef,
                scopedName);

        typeReferenceVisitor.onNamespaceEnd();

        Mockito.verify(compilationUnitModel).replaceTypeReference(
                same(compilationUnit),
                eq(parseTreeRef),
                eq(typeNo),
                eq(typeName));

        Mockito.verifyNoMoreInteractions(parsedFile, compilationUnitModel);
    }

    @Test
    public void testReplaceNonScopedTypeRef() throws IOException, ParserException {

        final ScopedName scopedName = ScopedName.makeScopedName(new String [] { "com", "test", "TestClass" });

        final TypeName typeName = new TypeName(
                new String [] { "com", "test" },
                null,
                "TestClass");

        final int typeNo = 321;

        final int parseTreeRef = 123;

        codeMap.addTypeMapping(typeName, typeNo);
        typeNameByScopedName.put(scopedName, typeName);

        final Object compilationUnit = new Object();

        Mockito.when(parsedFile.getCompilationUnit())
            .thenReturn(compilationUnit);
        
        final ParsedModuleAndCodeMap<ParsedFile> parsedModuleAndCodeMap
            = new ParsedModuleAndCodeMap<>(
                    new ParsedModuleAndCodeMap<>(Arrays.asList(parsedFile), -1, codeMap),
                    codeMap);
        
        final ReplaceTypeRefsPass<ParsedFile, Object> replaceTypeRefsPass
            = new ReplaceTypeRefsPass<>(compilationUnitModel);

        replaceTypeRefsPass.execute(parsedModuleAndCodeMap);

        Mockito.verify(parsedFile).getCompilationUnit();

        @SuppressWarnings("unchecked")
        final ArgumentCaptor<TypeReferenceVisitor<Object>> typeReferenceVisitorCaptor
            = ArgumentCaptor.forClass(TypeReferenceVisitor.class);

        Mockito.verify(compilationUnitModel).iterateTypeReferences(
                same(compilationUnit),
                typeReferenceVisitorCaptor.capture());

        final TypeReferenceVisitor<Object> typeReferenceVisitor = typeReferenceVisitorCaptor.getValue();

        typeReferenceVisitor.onNamespaceStart();
        typeReferenceVisitor.onNamespacePart("com");
        typeReferenceVisitor.onNamespacePart("test");

        typeReferenceVisitor.onNonScopedTypeReference(
                compilationUnit,
                parseTreeRef,
                "TestClass");

        typeReferenceVisitor.onNamespaceEnd();

        Mockito.verify(compilationUnitModel).replaceTypeReference(
                same(compilationUnit),
                eq(parseTreeRef),
                eq(typeNo),
                eq(typeName));

        Mockito.verifyNoMoreInteractions(parsedFile, compilationUnitModel);
    }
}
