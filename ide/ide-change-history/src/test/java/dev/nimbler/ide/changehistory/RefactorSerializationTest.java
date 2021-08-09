package dev.nimbler.ide.changehistory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import javax.xml.stream.XMLStreamException;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import dev.nimbler.build.types.resource.SourceFileResourcePath;

public class RefactorSerializationTest extends BaseSerializationChecks {

    @Test
    public void testSerialization() throws XMLStreamException, IOException {

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        final Path ideBasePath = ideBasePath();
        
        final List<SourceFileResourcePath> removedFiles = makeRemoveFiles(ideBasePath);
        
        final Map<SourceFileResourcePath, SourceFileResourcePath> movedFiles = makeMoveFiles(ideBasePath);
        
        final SourceFileComplexChange complexChange = new SourceFileComplexChange(
                ChangeReason.REFACTOR,
                null,
                null,
                removedFiles,
                movedFiles,
                null);
        
        RefactorSerializationHelper.serializeRefactor(complexChange, baos, ideBasePath);
        
        final byte [] xmlData = baos.toByteArray();
        
        assertThat(new String(xmlData)).isEqualTo(
                "<?xml version=\"1.0\" ?>"
                +   "<refactor>"
                +   "<removes>"
                +       "<remove>project/module/the/removed1/file1</remove>"
                +       "<remove>project/module/the/removed2/file2</remove>"
                +       "<remove>project/module/the/removed3/file3</remove>"
                +   "</removes>"
                +   "<moves>"
                +       "<move>"
                +           "<from>project/module/the/moved/file/source</from>"
                +           "<to>project/module/the/moved/file/destination</to>"
                +       "</move>"
                +       "<move>"
                +           "<from>project/module/other/moved/file/source</from>"
                +           "<to>project/module/other/moved/file/destination</to>"
                +       "</move>"
                +   "</moves>"
                + "</refactor>");
        
        
        final PathComplexChange deserialized = RefactorDeserializationHelper.deserializeRefactorFile(
                new ByteArrayInputStream(xmlData),
                ChangeReason.REFACTOR,
                null);
        
        assertThat(deserialized).isNotNull();

        checkRemovedFiles(deserialized);

        checkMovedFiles(deserialized);
    }
}


