package dev.nimbler.ide.core.ui.actions;

import dev.nimbler.ide.common.model.clipboard.Clipboard;
import dev.nimbler.ide.common.ui.actions.ActionAppParameters;
import dev.nimbler.ide.core.ui.controller.UndoRedoBuffer;
import dev.nimbler.ide.core.ui.model.dialogs.FindReplaceDialogModel;

public interface ActionApplicableParameters extends ActionAppParameters {

	Clipboard getClipboard();
	
	UndoRedoBuffer getUndoRedoBuffer();

	FindReplaceDialogModel getFindReplaceModel();
}
