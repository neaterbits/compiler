package dev.nimbler.ide.changehistory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.jutils.coll.MapOfList;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.model.text.TextEdit;

final class SourceFileComplexChange extends BaseComplexChange<SourceFileResourcePath> {
    
    private final ChangeRef historyRef;

    SourceFileComplexChange(
            ChangeReason change,
            ChangeRef historicRef, // if historic move
            List<SourceFileResourcePath> addFiles,
            List<SourceFileResourcePath> removeFiles,
            Map<SourceFileResourcePath, SourceFileResourcePath> moveFiles,
            MapOfList<SourceFileResourcePath, TextEdit> edits) {

        super(change, addFiles, removeFiles, moveFiles, edits);
        
        if (change.isHistoryMove()) {
            Objects.requireNonNull(historicRef);
        }
        else {
            if (historicRef != null) {
                throw new IllegalArgumentException();
            }
        }
        
        this.historyRef = historicRef;
    }

    ChangeRef getHistoryRef() {
        return historyRef;
    }
}
