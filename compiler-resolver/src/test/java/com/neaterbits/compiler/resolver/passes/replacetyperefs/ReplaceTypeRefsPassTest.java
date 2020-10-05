package com.neaterbits.compiler.resolver.passes.replacetyperefs;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.neaterbits.build.types.ScopedName;
import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.codemap.compiler.CompilerCodeMap;
import com.neaterbits.compiler.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.compiler.model.common.CompilationUnitModel;
import com.neaterbits.compiler.model.common.TypeReferenceVisitor;
import com.neaterbits.compiler.resolver.passes.ParsedFilesAndCodeMap;
import com.neaterbits.compiler.resolver.passes.typefinder.FoundTypeFiles;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.util.parse.ParserException;

public class ReplaceTypeRefsPassTest {

    private ParsedFile parsedFile;
    private CompilationUnitModel<Object> compilationUnitModel;
    private ParsedFilesAndCodeMap<ParsedFile> parsedFilesAndCodeMap;
    private Map<ScopedName, TypeName> typeNameByScopedName;
    private CompilerCodeMap codeMap;

    private ReplaceTypeRefsPass<ParsedFile, Object> replaceTypeRefsPass;

    @Before
    public void init() {

        this.parsedFile = Mockito.mock(ParsedFile.class);

        @SuppressWarnings("unchecked")
        final CompilationUnitModel<Object> compilationUnitModel
            = Mockito.mock(CompilationUnitModel.class);

        this.compilationUnitModel = compilationUnitModel;

        this.typeNameByScopedName = new HashMap<>();

        this.codeMap = new IntCompilerCodeMap();

        this.parsedFilesAndCodeMap = new ParsedFilesAndCodeMap<>(Arrays.asList(parsedFile), codeMap);

        this.replaceTypeRefsPass = new ReplaceTypeRefsPass<>(compilationUnitModel);
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
        typeNameByScopedName.put(scopedName, typeName);

        final FoundTypeFiles<ParsedFile> found = new FoundTypeFiles<>(
                parsedFilesAndCodeMap,
                typeNameByScopedName);

        final Object compilationUnit = new Object();

        Mockito.when(parsedFile.getCompilationUnit())
            .thenReturn(compilationUnit);

        replaceTypeRefsPass.execute(found);

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

        final FoundTypeFiles<ParsedFile> found = new FoundTypeFiles<>(
                parsedFilesAndCodeMap,
                typeNameByScopedName);

        final Object compilationUnit = new Object();

        Mockito.when(parsedFile.getCompilationUnit())
            .thenReturn(compilationUnit);

        replaceTypeRefsPass.execute(found);

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
