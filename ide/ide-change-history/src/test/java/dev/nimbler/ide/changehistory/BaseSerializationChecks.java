package dev.nimbler.ide.changehistory;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.jutils.ArrayUtils;
import org.jutils.PathUtil;
import org.jutils.coll.MapOfList;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.model.text.TextAdd;
import dev.nimbler.ide.model.text.TextEdit;
import dev.nimbler.ide.model.text.TextRemove;
import dev.nimbler.ide.model.text.TextReplace;
import dev.nimbler.ide.util.ui.text.StringText;

public abstract class BaseSerializationChecks extends BaseSerializationTest {

    static final String [] editFilePath = ArrayUtils.of("the", "edit", "file");
    static final String [] otherEditFilePath = ArrayUtils.of("other", "edited", "file");

    private static final String addedText = "added text";
    private static final String replacedText = "replaced text";
    private static final String updatedText = "updated text";
    private static final String removedText = "removed text";

    static MapOfList<SourceFileResourcePath, TextEdit> makeTestEdits() {
        
        final MapOfList<SourceFileResourcePath, TextEdit> edits
            = new MapOfList<>();

        final Path ideBasePath = ideBasePath();
        
        final SourceFileResourcePath editFile = makeEditSourceFilePath(ideBasePath);
        
        final TextAdd add = makeTextAdd();
        final TextReplace replace = makeTextReplace();
        
        edits.add(editFile, add);
        edits.add(editFile, replace);
        
        final SourceFileResourcePath otherEditFile = makeOtherEditSourceFilePath(ideBasePath);
        
        final TextRemove remove = makeTextRemove();
        
        edits.add(otherEditFile, remove);

        return edits;
    }
    
    static SourceFileResourcePath makeEditSourceFilePath(Path ideBasePath) {

        return makeSourceFileResourcePath(ideBasePath, editFilePath);
    }

    static SourceFileResourcePath makeOtherEditSourceFilePath(Path ideBasePath) {

        return makeSourceFileResourcePath(ideBasePath, otherEditFilePath);
    }
    
    static TextAdd makeTextAdd() {
        return new TextAdd(0L, new StringText(addedText));
    }
    
    static TextReplace makeTextReplace() {
        
        return new TextReplace(
                3L,
                replacedText.length(),
                new StringText(replacedText),
                new StringText(updatedText));
    }
    
    static TextRemove makeTextRemove() {
        
        return new TextRemove(1L, removedText.length(), new StringText(removedText));
    }
    
    static void checkAdd(TextEdit add) {
        
        checkAdd(add, 0L, addedText);
    }

    static void checkAdd(TextEdit add, long startPos, String text) {

        assertThat(add).isInstanceOf(TextAdd.class);
        
        final TextAdd textAdd = (TextAdd)add;
        
        assertThat(textAdd.getStartPos()).isEqualTo(startPos);
        assertThat(textAdd.getNewText()).isEqualTo(new StringText(text));
    }

    static void checkReplace(TextEdit replace) {
        
        assertThat(replace).isInstanceOf(TextReplace.class);

        final TextReplace textReplace = (TextReplace)replace;
        
        assertThat(textReplace.getStartPos()).isEqualTo(3L);
        assertThat(textReplace.getOldLength()).isEqualTo(replacedText.length());
        assertThat(textReplace.getOldText()).isEqualTo(new StringText(replacedText));
        assertThat(textReplace.getNewText()).isEqualTo(new StringText(updatedText));
    }
    
    static void checkRemove(TextEdit remove) {

        assertThat(remove).isInstanceOf(TextRemove.class);

        final TextRemove textRemove = (TextRemove)remove;

        assertThat(textRemove.getStartPos()).isEqualTo(1L);
        assertThat(textRemove.getOldLength()).isEqualTo(removedText.length());
        assertThat(textRemove.getOldText()).isEqualTo(new StringText(removedText));
    }

    private static final String [] addedFilePath1 = ArrayUtils.of("the", "added1", "file1");
    private static final String [] addedFilePath2 = ArrayUtils.of("the", "added2", "file2");
    private static final String [] addedFilePath3 = ArrayUtils.of("the", "added3", "file3");

    static List<SourceFileResourcePath> makeAddedFiles(Path ideBasePath) {

        return makeFiles(ideBasePath, addedFilePath1, addedFilePath2, addedFilePath3);
    }
    
    private static final String [] removedFilePath1 = ArrayUtils.of("the", "removed1", "file1");
    private static final String [] removedFilePath2 = ArrayUtils.of("the", "removed2", "file2");
    private static final String [] removedFilePath3 = ArrayUtils.of("the", "removed3", "file3");

    static List<SourceFileResourcePath> makeRemoveFiles(Path ideBasePath) {
        
        return makeFiles(ideBasePath, removedFilePath1, removedFilePath2, removedFilePath3);
    }
    
    static List<SourceFileResourcePath> makeFiles(Path ideBasePath, String [] ... paths) {
        
        final List<SourceFileResourcePath> list = new ArrayList<>(paths.length);
        
        for (String [] path : paths) {

            final SourceFileResourcePath sourceFileResourcePath = makeSourceFileResourcePath(ideBasePath, path);
        
            list.add(sourceFileResourcePath);
        }
        
        return list;
    }

    private static final String [] movedFile1PathSource = ArrayUtils.of("the", "moved", "file", "source");
    private static final String [] movedFile1PathDestination = ArrayUtils.of("the", "moved", "file", "destination");

    private static final String [] movedFile2PathSource = ArrayUtils.of("other", "moved", "file", "source");
    private static final String [] movedFile2PathDestination = ArrayUtils.of("other", "moved", "file", "destination");
    
    static LinkedHashMap<SourceFileResourcePath, SourceFileResourcePath> makeMoveFiles(Path ideBasePath) {

        final SourceFileResourcePath movedFile1Source = makeSourceFileResourcePath(ideBasePath, movedFile1PathSource);
        final SourceFileResourcePath movedFile1Destination = makeSourceFileResourcePath(ideBasePath, movedFile1PathDestination);


        final SourceFileResourcePath movedFile2Source = makeSourceFileResourcePath(ideBasePath, movedFile2PathSource);
        final SourceFileResourcePath movedFile2Destination = makeSourceFileResourcePath(ideBasePath, movedFile2PathDestination);

        final LinkedHashMap<SourceFileResourcePath, SourceFileResourcePath> movedFiles = new LinkedHashMap<>();
        
        movedFiles.put(movedFile1Source, movedFile1Destination);
        movedFiles.put(movedFile2Source, movedFile2Destination);
        
        return movedFiles;
    }

    static void checkAddedFiles(HistoricChange deserialized) {

        final List<Path> addedPaths = deserialized.getAdded();
        
        assertThat(addedPaths).isNotNull();
        assertThat(addedPaths.size()).isEqualTo(3);
        
        assertThat(PathUtil.getForwardSlashPathString(addedPaths.get(0)))
            .isEqualTo(PathUtil.getForwardSlashPathString(makeProjectModulePath(addedFilePath1)));

        assertThat(PathUtil.getForwardSlashPathString(addedPaths.get(1)))
            .isEqualTo(PathUtil.getForwardSlashPathString(makeProjectModulePath(addedFilePath2)));

        assertThat(PathUtil.getForwardSlashPathString(addedPaths.get(2)))
            .isEqualTo(PathUtil.getForwardSlashPathString(makeProjectModulePath(addedFilePath3)));
    }

    static void checkRemovedFiles(HistoricChange deserialized) {

        final List<Path> removedPaths = deserialized.getRemoved();
        
        assertThat(removedPaths).isNotNull();
        assertThat(removedPaths.size()).isEqualTo(3);
        
        assertThat(PathUtil.getForwardSlashPathString(removedPaths.get(0)))
            .isEqualTo(PathUtil.getForwardSlashPathString(makeProjectModulePath(removedFilePath1)));

        assertThat(PathUtil.getForwardSlashPathString(removedPaths.get(1)))
            .isEqualTo(PathUtil.getForwardSlashPathString(makeProjectModulePath(removedFilePath2)));

        assertThat(PathUtil.getForwardSlashPathString(removedPaths.get(2)))
            .isEqualTo(PathUtil.getForwardSlashPathString(makeProjectModulePath(removedFilePath3)));
    }

    static void checkMovedFiles(HistoricChange deserialized) {
        
        final Map<Path, Path> movedPaths = deserialized.getMoved();
        
        assertThat(movedPaths).isNotNull();
        assertThat(movedPaths.size()).isEqualTo(2);
        
        final Path moved1Destination = movedPaths.get(makeProjectModulePath(movedFile1PathSource));
        
        assertThat(PathUtil.getForwardSlashPathString(moved1Destination))
            .isEqualTo(PathUtil.getForwardSlashPathString(makeProjectModulePath(movedFile1PathDestination)));

        final Path moved2Destination = movedPaths.get(makeProjectModulePath(movedFile2PathSource));

        assertThat(PathUtil.getForwardSlashPathString(moved2Destination))
            .isEqualTo(PathUtil.getForwardSlashPathString(makeProjectModulePath(movedFile2PathDestination)));
    }
}



