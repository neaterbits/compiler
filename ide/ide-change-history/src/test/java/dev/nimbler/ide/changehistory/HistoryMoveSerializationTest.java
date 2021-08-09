package dev.nimbler.ide.changehistory;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.stream.XMLStreamException;

import org.junit.Test;

import dev.nimbler.build.types.resource.SourceFileResourcePath;

public class HistoryMoveSerializationTest extends BaseSerializationChecks {

    @Test
    public void testSerialization() throws XMLStreamException, IOException {

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        final Path ideBasePath = ideBasePath();

        final List<SourceFileResourcePath> addedFiles = makeAddedFiles(ideBasePath);

        final List<SourceFileResourcePath> removedFiles = makeRemoveFiles(ideBasePath);
        
        final Map<SourceFileResourcePath, SourceFileResourcePath> movedFiles = makeMoveFiles(ideBasePath);
        
        final SourceFileComplexChange complexChange = new SourceFileComplexChange(
                ChangeReason.MOVE,
                new UUIDChangeRefImpl(UUID.randomUUID(), UUID.randomUUID()),
                addedFiles,
                removedFiles,
                movedFiles,
                null);
        
        RefactorSerializationHelper.serializeHistoryMove(complexChange, baos, ideBasePath);
        
        final byte [] xmlData = baos.toByteArray();
        
        assertThat(new String(xmlData)).isEqualTo(
                "<?xml version=\"1.0\" ?>"
                +   "<historyMove>"
                +   "<adds>"
                +       "<add>project/module/the/added1/file1</add>"
                +       "<add>project/module/the/added2/file2</add>"
                +       "<add>project/module/the/added3/file3</add>"
                +   "</adds>"
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
                + "</historyMove>");
        
        
        final PathComplexChange deserialized = RefactorDeserializationHelper.deserializeHistoryMoveFile(
                new ByteArrayInputStream(xmlData),
                ChangeReason.MOVE,
                null);
        
        assertThat(deserialized).isNotNull();

        checkAddedFiles(deserialized);

        checkRemovedFiles(deserialized);

        checkMovedFiles(deserialized);
    }

}
