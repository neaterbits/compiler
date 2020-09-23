package com.neaterbits.compiler.codemap.compiler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.neaterbits.compiler.util.TypeName;

public class IntCompilerCodeMapTest {

    @Test
    public void testAddAndRemoveFiles() {

        final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();

        final int someFile = codeMap.addFile("someFile", new int [] { 0, 1, 2 });
        final int someOtherFile = codeMap.addFile("someOtherFile", new int [] { 3, 4, 5 });

        assertThat(codeMap.getFileNo("someFile")).isEqualTo(someFile);
        assertThat(codeMap.getFileName(someFile)).isEqualTo("someFile");

        assertThat(codeMap.getFileNo("someOtherFile")).isEqualTo(someOtherFile);
        assertThat(codeMap.getFileName(someOtherFile)).isEqualTo("someOtherFile");

        codeMap.removeFile("someFile");

        assertThat(codeMap.getFileNo("someFile")).isEqualTo(-1);
        assertThat(codeMap.getFileName(someFile)).isEqualTo(null);

        assertThat(codeMap.getFileNo("someOtherFile")).isEqualTo(someOtherFile);
        assertThat(codeMap.getFileName(someOtherFile)).isEqualTo("someOtherFile");
    }

    @Test
    public void testAddNullFileThrowsException() {

        final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();

        try {
            codeMap.addFile(null, new int [] { 1, 2, 3 });

            fail("Expected exception");
        }
        catch (NullPointerException ex) {

        }
    }

    @Test
    public void testRemoveUnknownFileThrowsException() {

        final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();

        codeMap.addFile("someFile", new int [] { 1, 2, 3 });

        try {
            codeMap.removeFile("someOtherFile");

            fail("Expected exception");
        }
        catch (IllegalStateException ex) {

        }

    }

    @Test
    public void testAddEmptyFileThrowsException() {

        final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();

        try {
            codeMap.addFile("", new int [] { 1, 2, 3 });

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {

        }
    }

    @Test
    public void testAddFileNameWithPrecedingSpacesThrowsException() {

        final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();

        try {
            codeMap.addFile(" someFile", new int [] { 1, 2, 3 });

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {

        }
    }

    @Test
    public void testAddFileNameWithTrailingSpacesThrowsException() {

        final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();

        try {
            codeMap.addFile("someFile ", new int [] { 1, 2, 3 });

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {

        }
    }

    @Test
    public void testAddAlreadyAddedThrowsException() {

        final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();

        codeMap.addFile("someFile", new int [] { 1, 2, 3 });

        try {
            codeMap.addFile("someFile", new int [] { 1, 2, 3 });

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {

        }
    }

    @Test
    public void testAddBeyondMaxThrowsException() {

        final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();

        int i;

        for (i = 0; i < BitDefs.MAX_SOURCE_FILE - 1; ++ i) {
            codeMap.addFile("someFile" + i, new int [] { 1, 2, 3 });
        }

        ++ i;

        try {
            codeMap.addFile("someFile" + i, new int [] { 1, 2, 3 });

            fail("Expected exception");
        }
        catch (IllegalStateException ex) {

        }
    }

    @Test
    public void testAddTypeNameMapping() {

        final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();

        final TypeName typeName = new TypeName(
                new String [] { "com", "test" },
                null,
                "TestType");

        final int typeNo = 1;

        codeMap.addTypeMapping(typeName, typeNo);

        assertThat(codeMap.getTypeNoByTypeName(typeName)).isEqualTo(typeNo);
    }

    @Test
    public void testAddTokens() {

        final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();

        final int someFile = codeMap.addFile("someFile", new int [] { 0, 1, 2 });

        final int parseTreeRef = 15;

        final int token = codeMap.addToken(someFile, parseTreeRef);

        assertThat(codeMap.getTokenForParseTreeRef(someFile, parseTreeRef)).isEqualTo(token);
        assertThat(codeMap.getParseTreeRefForToken(token)).isEqualTo(parseTreeRef);
    }

    @Test
    public void testAddTokenWhenNoSourceFilesThrowsException() {

        final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();

        try {
            codeMap.addToken(1, 15);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {

        }
    }

    @Test
    public void testAddTokenToUnknownSourceFileThrowsException() {

        final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();

        final int fileNo = codeMap.addFile("someFile", new int [] { 0, 1, 2 });

        try {
            codeMap.addToken(fileNo + 1, 15);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {

        }
    }

    @Test
    public void testAddTokenToSourceFileNoOutOfRangeThrowsException() {

        final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();

        codeMap.addFile("someFile", new int [] { 0, 1, 2 });

        try {
            codeMap.addToken(1000000, 15);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {

        }
    }

    @Test
    public void testAddVariableCrossReference() {

        final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();

        final int someFile = codeMap.addFile("someFile", new int [] { 0, 1, 2 });
        final int someOtherFile = codeMap.addFile("someOtherFile", new int [] { 0, 1, 2 });

        final int parseTreeRef1 = 15;

        final int parseTreeRef2 = 3;

        final int token1 = codeMap.addToken(someFile, parseTreeRef1);
        final int token2 = codeMap.addToken(someOtherFile, parseTreeRef2);

        codeMap.addTokenVariableReference(token1, token2);

        assertThat(codeMap.getVariableDeclarationTokenReferencedFrom(token1))
            .isEqualTo(token2);

    }
}
