package com.neaterbits.ide.common.ui.controller;

import com.neaterbits.ide.common.model.source.SourceFileModel;

public interface EditorsListener {

    void onSourceFileModelChanged(SourceFileModel model);

    void onEditorCursorPosUpdate(long cursorOffset);

}
