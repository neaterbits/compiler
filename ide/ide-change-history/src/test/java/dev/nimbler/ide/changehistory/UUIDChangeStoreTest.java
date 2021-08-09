package dev.nimbler.ide.changehistory;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.jutils.ArrayUtils;
import org.jutils.IOUtils;
import org.jutils.PathUtil;
import org.jutils.coll.MapOfList;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.changehistory.changeoutput.ChangeHistoryStorageFactory;
import dev.nimbler.ide.changehistory.filestorage.FileRetrieval;
import dev.nimbler.ide.changehistory.filestorage.FileStorage;
import dev.nimbler.ide.model.text.TextAdd;
import dev.nimbler.ide.model.text.TextEdit;
import dev.nimbler.ide.util.ui.text.StringText;

public class UUIDChangeStoreTest extends BaseSerializationTest {

    @Test
    public void testStoreAndRead() throws Exception {
        
        final File tempDir = Files.createTempDirectory("uuidchangestoretest").toFile();
        
        tempDir.mkdirs();
        
        final int filesPerDir = 3;
        
        int numChanges = 0;
        
        try {
            final UUIDChangeStore writeChangeStore = new UUIDChangeStore(tempDir.toPath(), filesPerDir);

            try {
                writeEditChange(writeChangeStore, numChanges ++);
                writeEditChange(writeChangeStore, numChanges ++);
                writeEditChange(writeChangeStore, numChanges ++);
                writeEditChange(writeChangeStore, numChanges ++);
                
                assertThat(numChanges).isGreaterThan(filesPerDir);
            }
            finally {
                writeChangeStore.close();
            }

            final UUIDChangeStore readChangeStore = new UUIDChangeStore(tempDir.toPath(), filesPerDir);

            
            try {
                checkEditFiles(readChangeStore, true, numChanges);
                checkEditFiles(readChangeStore, false, numChanges);
            }
            finally {
                readChangeStore.close();
            }
        }
        finally {
            org.jutils.Files.deleteRecursively(tempDir);
        }
    }
    
    private static void checkEditFiles(ChangeStore readChangeStore, boolean chronological, int numChanges) throws Exception {
        
        final List<ChangeRef> chronologicalRefs = new ArrayList<>();

        readChangeStore.iterateChanges(
                chronological,
                (ref, historicRef) -> {
                    
                    chronologicalRefs.add(ref);
                    
                    return IteratorContinuation.CONTINUE;
                });
        
        assertThat(chronologicalRefs.size()).isEqualTo(numChanges);

        checkEditFiles(readChangeStore, chronologicalRefs, chronological);
    }
    private static void checkEditFiles(ChangeStore readChangeStore, List<ChangeRef> refs, boolean chronological) throws Exception {
        
        int fileNo = chronological ? 0 : refs.size() - 1;
        
        for (ChangeRef ref : refs) {
            
            try (FileRetrieval retrieval = readChangeStore.createEditsChangeInput(ref)) {
             
                final Path path = editsChangePath(fileNo);
                
                try (InputStream inputStream = retrieval.retrieveFile(path)) {
                 
                    assertThat(inputStream).isNotNull();
                    
                    final String contents = IOUtils.readUnicode(inputStream);
                
                    assertThat(contents).isEqualTo(editsFileText(fileNo));
                }
                
                if (chronological) {
                    ++ fileNo;
                }
                else {
                    -- fileNo;
                }
            }
        }
    }

    private static String [] editsChangeParts(int fileNo) {
        
        return ArrayUtils.of("path", "to", "the", "edits", "file" + fileNo);
    }

    private static Path editsChangePath(int fileNo) {

        final Path path = PathUtil.pathFromParts(editsChangeParts(fileNo));
        
        return path;
    }
    
    private static String editsFileText(int fileNo) {
        
        return "theFileText" + fileNo;
    }
    
    private static void writeEditChange(ChangeStore changeStore, int fileNo) throws Exception {
        
        final SourceFileResourcePath sourceFile = makeSourceFileResourcePath(ideBasePath(), editsChangeParts(fileNo));
                
        final TextEdit add = new TextAdd(0L, new StringText("added text " + fileNo));
    
        final MapOfList<SourceFileResourcePath, TextEdit> edits = new MapOfList<>();
        
        edits.add(sourceFile, add);
        
        final SourceFileComplexChange change = new SourceFileComplexChange(
                ChangeReason.EDIT,
                null,
                null,
                null,
                null,
                edits);
    
        try (ChangeHistoryStorageFactory changeOutputFactory = changeStore.prepare(change)) {
    
            writeEditsFile(changeOutputFactory, fileNo);
        }
    }

    private static void writeEditsFile(ChangeHistoryStorageFactory changeOutputFactory, int fileNo) throws Exception {
        
        final Path editsChangePath = editsChangePath(fileNo);
        
        try (FileStorage editsChangeOutput = changeOutputFactory.createEditsChangeOutput()) {

            try (OutputStream stream = editsChangeOutput.addFile(editsChangePath)) {

                final String fileText = editsFileText(fileNo);

                stream.write(fileText.getBytes());
            }
        }
    }
}
