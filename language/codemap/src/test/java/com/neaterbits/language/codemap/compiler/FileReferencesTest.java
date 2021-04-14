package com.neaterbits.language.codemap.compiler;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

import com.neaterbits.language.codemap.ArrayAllocation;
import com.neaterbits.language.codemap.compiler.FileReferences;

public class FileReferencesTest {

    @Test
    public void addAndRemoveFile() {

        final FileReferences fileReferences = new FileReferences();

        final int fileNo = 1;
        final int [] types = new int [] { 1, 2, 3 };

        fileReferences.addFile(fileNo, types);

        assertThat(fileReferences.getTypesForFile(fileNo)).isEqualTo(types);

        fileReferences.removeFile(fileNo);

        assertThat(fileReferences.getTypesForFile(fileNo)).isNull();
    }

    @Test
    public void testGetOutOfRangeThrowsException() {

        final FileReferences fileReferences = new FileReferences();

        final int fileNo = 1;
        final int [] types = new int [] { 1, 2, 3 };

        fileReferences.addFile(fileNo, types);

        try {
            fileReferences.getTypesForFile(ArrayAllocation.DEFAULT_LENGTH);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {

        }
    }

    @Test
    public void testAddAlreadyAddedThrowsException() {

        final FileReferences fileReferences = new FileReferences();

        final int fileNo = 1;
        final int [] types = new int [] { 1, 2, 3 };

        fileReferences.addFile(fileNo, types);

        try {
            fileReferences.addFile(fileNo, types);

            fail("Expected exception");
        }
        catch (IllegalStateException ex) {

        }
    }

    @Test
    public void addNullTypesReturnsEmptyArray() {

        final FileReferences fileReferences = new FileReferences();

        final int fileNo = 1;

        fileReferences.addFile(fileNo, null);

        assertThat(fileReferences.getTypesForFile(fileNo)).isEqualTo(new int [0]);

        fileReferences.removeFile(fileNo);

        assertThat(fileReferences.getTypesForFile(fileNo)).isNull();
    }
}
