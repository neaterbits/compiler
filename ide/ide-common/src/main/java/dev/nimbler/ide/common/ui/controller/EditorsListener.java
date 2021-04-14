package dev.nimbler.ide.common.ui.controller;

import dev.nimbler.ide.common.model.source.SourceFileModel;

public interface EditorsListener {

    void onSourceFileModelChanged(SourceFileModel model);

    void onEditorCursorPosUpdate(long cursorOffset);

}
