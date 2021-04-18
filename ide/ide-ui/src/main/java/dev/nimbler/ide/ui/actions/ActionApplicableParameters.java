package dev.nimbler.ide.ui.actions;

import dev.nimbler.ide.common.model.clipboard.Clipboard;
import dev.nimbler.ide.common.ui.actions.ActionAppParameters;
import dev.nimbler.ide.ui.controller.UndoRedoBuffer;
import dev.nimbler.ide.ui.model.dialogs.FindReplaceDialogModel;

public interface ActionApplicableParameters extends ActionAppParameters {

	Clipboard getClipboard();
	
	UndoRedoBuffer getUndoRedoBuffer();

	FindReplaceDialogModel getFindReplaceModel();
}
