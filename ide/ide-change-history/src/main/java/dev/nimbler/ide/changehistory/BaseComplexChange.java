package dev.nimbler.ide.changehistory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jutils.coll.MapOfList;

import dev.nimbler.ide.model.text.TextEdit;

abstract class BaseComplexChange<PATH> {

    private final ChangeReason changeReason;
    
    private final List<PATH> addFiles;
    private final List<PATH> removeFiles;
    private final Map<PATH, PATH> moveFiles;
    private final MapOfList<PATH, TextEdit> edits;

    BaseComplexChange(
            ChangeReason changeReason,
            List<PATH> addFiles,
            List<PATH> removeFiles,
            Map<PATH, PATH> moveFiles,
            MapOfList<PATH, TextEdit> edits) {
    
        Objects.requireNonNull(changeReason);
        
        this.changeReason = changeReason;
        this.addFiles = addFiles;
        this.removeFiles = removeFiles;
        this.moveFiles = moveFiles;
        this.edits = edits;
    }

    final ChangeReason getChangeReason() {
        return changeReason;
    }

    final List<PATH> getAddFiles() {
        return addFiles;
    }

    final List<PATH> getRemoveFiles() {
        return removeFiles;
    }

    final Map<PATH, PATH> getMoveFiles() {
        return moveFiles;
    }

    final MapOfList<PATH, TextEdit> getEdits() {
        return edits;
    }
}
