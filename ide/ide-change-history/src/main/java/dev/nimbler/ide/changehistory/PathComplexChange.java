package dev.nimbler.ide.changehistory;

import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.jutils.coll.MapOfList;
import org.jutils.coll.UnmodifiableMapOfList;

import dev.nimbler.ide.model.text.TextEdit;

final class PathComplexChange extends BaseComplexChange<Path> implements HistoricChange {

    PathComplexChange(
            ChangeReason changeReason,
            List<Path> addFiles,
            List<Path> removeFiles,
            Map<Path, Path> moveFiles,
            MapOfList<Path, TextEdit> edits) {

        super(changeReason, addFiles, removeFiles, moveFiles, edits);
    }

    @Override
    public List<Path> getAdded() {

        final List<Path> added = getAddFiles();
        
        return added != null
                ? Collections.unmodifiableList(added)
                : null;
    }

    @Override
    public List<Path> getRemoved() {
        
        final List<Path> removed = getRemoveFiles();
        
        return removed != null
                ? Collections.unmodifiableList(removed)
                : null;
    }

    @Override
    public Map<Path, Path> getMoved() {
        
        final Map<Path, Path> moved = getMoveFiles();
        
        return moved != null
                ? Collections.unmodifiableMap(moved)
                : null;
    }

    @Override
    public UnmodifiableMapOfList<Path, TextEdit> getEdited() {

        return getEdits();
    }
}
