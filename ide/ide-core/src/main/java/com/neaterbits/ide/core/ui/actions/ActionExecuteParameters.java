package com.neaterbits.ide.core.ui.actions;

import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.model.clipboard.Clipboard;
import com.neaterbits.ide.common.model.codemap.CodeMapModel;
import com.neaterbits.ide.common.ui.actions.ActionExeParameters;
import com.neaterbits.ide.common.ui.controller.EditorActions;
import com.neaterbits.ide.common.ui.controller.EditorsActions;
import com.neaterbits.ide.common.ui.view.View;
import com.neaterbits.ide.component.common.ComponentIDEAccess;
import com.neaterbits.ide.component.common.IDEComponentsConstAccess;
import com.neaterbits.ide.core.ui.controller.UndoRedoBuffer;
import com.neaterbits.ide.core.ui.model.dialogs.FindReplaceDialogModel;
import com.neaterbits.ide.core.ui.view.UIDialogs;

public interface ActionExecuteParameters extends ActionExeParameters {

	IDEComponentsConstAccess getComponents();

	SourceFileResourcePath getCurrentEditedFile();
	
	UIDialogs getUIDialogs();

	Clipboard getClipboard();
	
	UndoRedoBuffer getUndoRedoBuffer();
	
	ComponentIDEAccess getComponentIDEAccess();
	
	BuildRoot getBuildRoot();
	
	EditorsActions getEditorsActions();
	
	View getFocusedView();
	
	EditorActions getFocusedEditor();
	
	CodeMapModel getCodeMap();
	
	FindReplaceDialogModel getFindReplaceModel();

	void storeFindReplaceModel(FindReplaceDialogModel findReplaceModel);
}
