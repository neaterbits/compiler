package dev.nimbler.ide.changehistory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.xml.stream.XMLStreamException;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.jutils.PathUtil;
import org.jutils.coll.MapOfList;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.model.text.TextEdit;

public class EditsSerializationTest extends BaseSerializationChecks {

    @Test
    public void testSerialization() throws XMLStreamException, IOException {
        
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        final MapOfList<SourceFileResourcePath, TextEdit> edits = makeTestEdits();

        final Path ideBasePath = ideBasePath();

        EditsSerializationHelper.serializeFileEdits(
                edits,
                baos,
                ideBasePath);

        final byte [] xmlData = baos.toByteArray();
        
        final String xml = new String(xmlData);
    
        assertThat(xml)
            .isEqualTo(
                    "<?xml version=\"1.0\" ?>"
                    +   "<fileEdits>"
                    +       "<file>"
                    +           "<path>project/module/the/edit/file</path>"
                    +           "<edits>"
                    +               "<add>"
                    +                   "<pos>0</pos>"
                    +                   "<added>added text</added>"
                    +                "</add>"
                    +               "<replace>"
                    +                   "<pos>3</pos>"
                    +                   "<replaced>replaced text</replaced>"
                    +                   "<update>updated text</update>"
                    +               "</replace>"
                    +           "</edits>"
                    +       "</file>"
                    +       "<file>"
                    +           "<path>project/module/other/edited/file</path>"
                    +           "<edits>"
                    +               "<remove>"
                    +                   "<pos>1</pos>"
                    +                   "<removed>removed text</removed>"
                    +               "</remove>"
                    +           "</edits>"
                    +       "</file>"
                    + "</fileEdits>");
        
        final MapOfList<Path, TextEdit> deserialized = EditsDeserializationHelper.deserializeEditFiles(new ByteArrayInputStream(xmlData));

        assertThat(deserialized.keyCount()).isEqualTo(2);
        
        final Path theEditFilePath = makeProjectModulePath(editFilePath);
        
        assertThat(PathUtil.getForwardSlashPathString(theEditFilePath))
            .isEqualTo("project/module/the/edit/file");
        
        final List<TextEdit> editsList = deserialized.get(theEditFilePath);
        
        assertThat(editsList).isNotNull();
        assertThat(editsList.size()).isEqualTo(2);
        
        checkAdd(editsList.get(0));
        checkReplace(editsList.get(1));
        
        final Path otherFile = makeProjectModulePath(otherEditFilePath);
        
        final List<TextEdit> otherEditsList = deserialized.get(otherFile);
        
        assertThat(otherEditsList).isNotNull();
        
        assertThat(otherEditsList.size()).isEqualTo(1);
        
        checkRemove(otherEditsList.get(0));
    }
}
