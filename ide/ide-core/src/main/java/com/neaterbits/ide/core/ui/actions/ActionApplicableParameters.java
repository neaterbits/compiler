package com.neaterbits.ide.core.ui.actions;

import com.neaterbits.ide.common.model.clipboard.Clipboard;
import com.neaterbits.ide.common.ui.actions.ActionAppParameters;
import com.neaterbits.ide.core.ui.controller.UndoRedoBuffer;
import com.neaterbits.ide.core.ui.model.dialogs.FindReplaceDialogModel;

public interface ActionApplicableParameters extends ActionAppParameters {

	Clipboard getClipboard();
	
	UndoRedoBuffer getUndoRedoBuffer();

	FindReplaceDialogModel getFindReplaceModel();
}
