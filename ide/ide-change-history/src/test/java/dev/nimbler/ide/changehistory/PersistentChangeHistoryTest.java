package dev.nimbler.ide.changehistory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.jutils.IOUtils;
import org.jutils.PathUtil;
import org.jutils.coll.MapOfList;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.model.text.TextAdd;
import dev.nimbler.ide.model.text.TextEdit;
import dev.nimbler.ide.util.ui.text.StringText;

public class PersistentChangeHistoryTest extends BaseSerializationChecks {

    @Test
    public void testTextEdits() throws Exception {
        
        final Path ideBasePath = ideBasePath();

        final File tempDir = Files.createTempDirectory("persistenchangehistorytest").toFile();
        
        final File storageDir = new File(tempDir, "storage");

        final File ideDir = new File(tempDir, "testidebasepath");

        final Path fullIdePath = ideDir.toPath().resolve(ideBasePath);
        
        storageDir.mkdirs();
        ideDir.mkdirs();

        final SourceFileResourcePath editFilePath = makeEditSourceFilePath(fullIdePath);
        final SourceFileResourcePath otherEditFilePath = makeOtherEditSourceFilePath(fullIdePath);
        
        try {
            try (PersistentChangeHistory changeHistory
                = new PersistentChangeHistory(storageDir.toPath(), fullIdePath)) {

                writeTestSourceFile(ideDir, editFilePath, "text add file");
                changeHistory.addTextEdit(editFilePath, makeTextAdd());

                writeTestSourceFile(ideDir, editFilePath, "text replace file");
                changeHistory.addTextEdit(editFilePath, makeTextReplace());

                writeTestSourceFile(ideDir, otherEditFilePath, "text remove file");
                changeHistory.addTextEdit(otherEditFilePath, makeTextRemove());
            }

            try (PersistentChangeHistory changeHistory
                    = new PersistentChangeHistory(storageDir.toPath(), fullIdePath)) {
                
                final List<ReasonChangeRef> changes = new ArrayList<>();

                changeHistory.iterateAllChanges(true, changes::add);

                assertThat(changes.size()).isEqualTo(3);
            
                checkChange(changeHistory, fullIdePath, changes.get(0), editFilePath, BaseSerializationChecks::checkAdd);
                checkPrevStateFile(changeHistory, fullIdePath, changes.get(0), editFilePath, "text add file");
                
                checkChange(changeHistory, fullIdePath, changes.get(1), editFilePath, BaseSerializationChecks::checkReplace);
                checkPrevStateFile(changeHistory, fullIdePath, changes.get(1), editFilePath, "text replace file");

                checkChange(changeHistory, fullIdePath, changes.get(2), otherEditFilePath, BaseSerializationChecks::checkRemove);
                checkPrevStateFile(changeHistory, fullIdePath, changes.get(2), otherEditFilePath, "text remove file");
            }
        }
        finally {
            org.jutils.Files.deleteRecursively(tempDir);
        }
    }
    
    @Test
    public void testMovedAndRemovedFiles() throws Exception {

        final Path ideBasePath = ideBasePath();

        final File tempDir = Files.createTempDirectory("persistenchangehistorytest").toFile();
        
        final File storageDir = new File(tempDir, "storage");

        final File ideDir = new File(tempDir, "testidebasepath");

        final Path fullIdePath = ideDir.toPath().resolve(ideBasePath);
        
        storageDir.mkdirs();
        ideDir.mkdirs();

        try {
            final List<SourceFileResourcePath> removeFiles = makeRemoveFiles(fullIdePath);
            assertThat(removeFiles.size()).isEqualTo(3);

            final LinkedHashMap<SourceFileResourcePath, SourceFileResourcePath> moveFiles
                = makeMoveFiles(fullIdePath);
            
            assertThat(moveFiles.size()).isEqualTo(2);

            final List<SourceFileResourcePath> moveSources = new ArrayList<>(moveFiles.keySet());

            try (PersistentChangeHistory changeHistory
                = new PersistentChangeHistory(storageDir.toPath(), fullIdePath)) {

                writeTestSourceFile(ideDir, removeFiles.get(0), "removed file 1");
                writeTestSourceFile(ideDir, removeFiles.get(1), "removed file 2");
                writeTestSourceFile(ideDir, removeFiles.get(2), "removed file 3");
                
                writeTestSourceFile(ideDir, moveSources.get(0), "moved file 1");
                writeTestSourceFile(ideDir, moveSources.get(1), "moved file 2");

                changeHistory.addRefactorChange(removeFiles, moveFiles, null);
            }

            try (PersistentChangeHistory changeHistory
                    = new PersistentChangeHistory(storageDir.toPath(), fullIdePath)) {
                
                final List<ReasonChangeRef> changes = new ArrayList<>();

                changeHistory.iterateAllChanges(true, changes::add);

                assertThat(changes.size()).isEqualTo(1);
            
                final ReasonChangeRef changeRef = changes.get(0);
                
                final HistoricChange historicChange = changeHistory.getHistoricChange(changeRef);
                
                assertThat(historicChange.getEdited()).isNull();

                assertThat(historicChange.getRemoved()).isNotNull();
                assertThat(historicChange.getRemoved().size()).isEqualTo(3);

                assertThat(PathUtil.getForwardSlashPathString(historicChange.getRemoved().get(0)))
                        .isEqualTo(makeProjectModulePathString(fullIdePath, removeFiles.get(0)));

                assertThat(PathUtil.getForwardSlashPathString(historicChange.getRemoved().get(1)))
                    .isEqualTo(makeProjectModulePathString(fullIdePath, removeFiles.get(1)));

                assertThat(PathUtil.getForwardSlashPathString(historicChange.getRemoved().get(2)))
                    .isEqualTo(makeProjectModulePathString(fullIdePath, removeFiles.get(2)));

                checkPrevStateFile(changeHistory, fullIdePath, changeRef, removeFiles.get(0), "removed file 1");
                checkPrevStateFile(changeHistory, fullIdePath, changeRef, removeFiles.get(1), "removed file 2");
                checkPrevStateFile(changeHistory, fullIdePath, changeRef, removeFiles.get(2), "removed file 3");

                assertThat(historicChange.getMoved()).isNotNull();
                assertThat(historicChange.getMoved().size()).isEqualTo(2);
                
                final Path moveSource1 = makeProjectModulePath(fullIdePath, moveSources.get(0));
                final Path moveSource2 = makeProjectModulePath(fullIdePath, moveSources.get(1));

                assertThat(historicChange.getMoved()).containsEntry(
                        moveSource1,
                        makeProjectModulePath(fullIdePath, moveFiles.get(moveSources.get(0))));

                assertThat(historicChange.getMoved()).containsEntry(
                        moveSource2,
                        makeProjectModulePath(fullIdePath, moveFiles.get(moveSources.get(1))));

                checkPrevStateFile(changeHistory, fullIdePath, changeRef, moveSources.get(0), "moved file 1");
                checkPrevStateFile(changeHistory, fullIdePath, changeRef, moveSources.get(1), "moved file 2");
            }
        }
        finally {
            org.jutils.Files.deleteRecursively(tempDir);
        }
    }
    
    private static class ChangeRefs {
        
        private final ReasonChangeRef reasonChangeRef;
        private final ChangeRef historicChangeRef;
        
        ChangeRefs(ReasonChangeRef reasonChangeRef, ChangeRef historicChangeRef) {
            this.reasonChangeRef = reasonChangeRef;
            this.historicChangeRef = historicChangeRef;
        }
    }
    
    @Test
    public void testHistoricMove() throws Exception {

        final Path ideBasePath = ideBasePath();

        final File tempDir = Files.createTempDirectory("persistenchangehistorytest").toFile();
        
        final File storageDir = new File(tempDir, "storage");

        final File ideDir = new File(tempDir, "testidebasepath");

        final Path fullIdePath = ideDir.toPath().resolve(ideBasePath);
        
        storageDir.mkdirs();
        ideDir.mkdirs();

        final SourceFileResourcePath editFilePath = makeEditSourceFilePath(fullIdePath);
        final SourceFileResourcePath otherEditFilePath = makeOtherEditSourceFilePath(fullIdePath);

        try {

            try (PersistentChangeHistory changeHistory
                    = new PersistentChangeHistory(storageDir.toPath(), fullIdePath)) {

                writeTestSourceFile(ideDir, editFilePath, "text add file");
                changeHistory.addTextEdit(editFilePath, makeTextAdd());

                writeTestSourceFile(ideDir, editFilePath, "text replace file");
                changeHistory.addTextEdit(editFilePath, makeTextReplace());

                writeTestSourceFile(ideDir, otherEditFilePath, "text remove file");
                changeHistory.addTextEdit(otherEditFilePath, makeTextRemove());
            }

            // Write a history move
            try (PersistentChangeHistory changeHistory
                    = new PersistentChangeHistory(storageDir.toPath(), fullIdePath)) {
                
                
                final List<ReasonChangeRef> changes = new ArrayList<>();

                changeHistory.iterateAllChanges(true, changes::add);

                assertThat(changes.size()).isEqualTo(3);

                writeTestSourceFile(ideDir, otherEditFilePath, "text undo file");

                final MapOfList<SourceFileResourcePath, TextEdit> edits = new MapOfList<>();
                
                edits.add(otherEditFilePath, new TextAdd(0L, new StringText("text remove undo")));
                
                changeHistory.addHistoryMoveChange(
                        ChangeReason.UNDO,
                        changes.get(2),
                        null,
                        null,
                        null,
                        edits);
            }

            try (PersistentChangeHistory changeHistory
                    = new PersistentChangeHistory(storageDir.toPath(), fullIdePath)) {
                
                final List<ChangeRefs> changes = new ArrayList<>();

                changeHistory.iterateChanges(true, (ref, historicRef) -> {
                    changes.add(new ChangeRefs(ref, historicRef));
                
                    return IteratorContinuation.CONTINUE;
                });

                assertThat(changes.size()).isEqualTo(4);
                
                final ChangeRefs changeRefs = changes.get(3);

                assertThat(changeRefs.reasonChangeRef.getChangeReason()).isEqualTo(ChangeReason.UNDO);
                
                assertThat(changeRefs.historicChangeRef).isNotNull();
                assertThat(changeRefs.historicChangeRef).isEqualTo(changes.get(2).reasonChangeRef);
                
                checkChange(changeHistory, fullIdePath, changeRefs.reasonChangeRef, otherEditFilePath, edit -> checkAdd(edit, 0L, "text remove undo"));
                checkPrevStateFile(changeHistory, fullIdePath, changeRefs.reasonChangeRef, otherEditFilePath, "text undo file");
            }
        }
        finally {
            org.jutils.Files.deleteRecursively(tempDir);
        }
    }

    private static void checkChange(
            PersistentChangeHistory changeHistory,
            Path ideBasePath,
            ReasonChangeRef changeRef,
            SourceFileResourcePath sourceFileResourcePath,
            Consumer<TextEdit> checkTextEdit) throws IOException {

        final HistoricChange change = changeHistory.getHistoricChange(changeRef);
        
        assertThat(change.getMoved()).isNull();
        assertThat(change.getRemoved()).isNull();
        
        assertThat(change.getEdited()).isNotNull();
        
        final Path modulePath = makeProjectModulePath(ideBasePath, sourceFileResourcePath);
        
        final List<TextEdit> edits = change.getEdited().getUnmodifiable(modulePath);
        
        assertThat(edits).isNotNull();
        assertThat(edits.size()).isEqualTo(1);
        
        checkTextEdit.accept(edits.get(0));
    }
    
    private static void writeTestSourceFile(
            File ideDir,
            SourceFileResourcePath sourceFileResourcePath,
            String text) throws IOException {
        
        final Path fullPath = ideDir.toPath().resolve(sourceFileResourcePath.getFile().toPath());
        
        fullPath.getParent().toFile().mkdirs();
    
        IOUtils.writeUnicode(fullPath.toFile(), text);
    }
    
    private static void checkPrevStateFile(
            PersistentChangeHistory changeHistory,
            Path ideBasePath,
            ChangeRef changeRef,
            SourceFileResourcePath sourceFileResourcePath,
            String text) throws IOException {

        final Path modulePath = makeProjectModulePath(ideBasePath, sourceFileResourcePath);

        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        
        changeHistory.deserializePrevStateFile(changeRef, modulePath, baos);
        
        assertThat(new String(baos.toByteArray())).isEqualTo(text);
    }
}
