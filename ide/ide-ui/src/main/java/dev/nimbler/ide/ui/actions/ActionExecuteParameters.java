package dev.nimbler.ide.ui.actions;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.codeaccess.CodeAccess;
import dev.nimbler.ide.common.model.clipboard.Clipboard;
import dev.nimbler.ide.common.ui.actions.ActionExeParameters;
import dev.nimbler.ide.common.ui.controller.EditorActions;
import dev.nimbler.ide.common.ui.controller.EditorsActions;
import dev.nimbler.ide.common.ui.view.View;
import dev.nimbler.ide.component.common.ComponentIDEAccess;
import dev.nimbler.ide.component.common.IDEComponentsConstAccess;
import dev.nimbler.ide.ui.controller.UndoRedoBuffer;
import dev.nimbler.ide.ui.model.dialogs.FindReplaceDialogModel;
import dev.nimbler.ide.ui.view.UIDialogs;

public interface ActionExecuteParameters extends ActionExeParameters {

	IDEComponentsConstAccess getComponents();

	SourceFileResourcePath getCurrentEditedFile();
	
	UIDialogs getUIDialogs();

	Clipboard getClipboard();
	
	UndoRedoBuffer getUndoRedoBuffer();
	
	ComponentIDEAccess getComponentIDEAccess();
	
	CodeAccess getCodeAccess();
	
	EditorsActions getEditorsActions();
	
	View getFocusedView();
	
	EditorActions getFocusedEditor();
	
	FindReplaceDialogModel getFindReplaceModel();

	void storeFindReplaceModel(FindReplaceDialogModel findReplaceModel);
}
