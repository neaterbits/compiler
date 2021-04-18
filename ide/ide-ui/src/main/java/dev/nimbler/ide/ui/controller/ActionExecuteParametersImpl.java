package dev.nimbler.ide.ui.controller;

import java.util.Objects;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.codeaccess.CodeAccess;
import dev.nimbler.ide.common.model.clipboard.Clipboard;
import dev.nimbler.ide.common.ui.controller.EditorActions;
import dev.nimbler.ide.common.ui.controller.EditorsActions;
import dev.nimbler.ide.common.ui.view.View;
import dev.nimbler.ide.component.common.ComponentIDEAccess;
import dev.nimbler.ide.component.common.IDEComponentsConstAccess;
import dev.nimbler.ide.ui.actions.ActionExecuteParameters;
import dev.nimbler.ide.ui.model.dialogs.FindReplaceDialogModel;
import dev.nimbler.ide.ui.view.UIDialogs;

final class ActionExecuteParametersImpl implements ActionExecuteParameters {

	private final ActionExecuteState executeState;
	private final View focusedView;
	private final EditorActions focusedEditor;
	private final SourceFileResourcePath currentEditedFile;
	
	ActionExecuteParametersImpl(
			ActionExecuteState executeState,
			View focusedView,
			EditorActions focusedEditor,
			SourceFileResourcePath currentEditedFile) {

		Objects.requireNonNull(executeState);
		
		this.executeState = executeState;
		this.focusedView = focusedView;
		this.focusedEditor = focusedEditor;
		this.currentEditedFile = currentEditedFile;
	}

	@Override
	public IDEComponentsConstAccess getComponents() {
		return executeState.getComponents();
	}

	@Override
	public SourceFileResourcePath getCurrentEditedFile() {
		return currentEditedFile;
	}

	@Override
	public UIDialogs getUIDialogs() {
		return executeState.getUIDialogs();
	}

	@Override
	public Clipboard getClipboard() {
		return executeState.getClipboard();
	}

	@Override
	public UndoRedoBuffer getUndoRedoBuffer() {
		return executeState.getUndoRedoBuffer();
	}

	@Override
	public ComponentIDEAccess getComponentIDEAccess() {
		return executeState.getComponentIDEAccess();
	}

	@Override
	public CodeAccess getCodeAccess() {
		return executeState.getCodeAccess();
	}

	@Override
	public View getFocusedView() {
		return focusedView;
	}

	@Override
	public EditorsActions getEditorsActions() {
		return executeState.getEditorsActions();
	}
	
	@Override
	public EditorActions getFocusedEditor() {
		return focusedEditor;
	}

	@Override
	public FindReplaceDialogModel getFindReplaceModel() {
		return executeState.getFindReplaceModel();
	}

	@Override
	public void storeFindReplaceModel(FindReplaceDialogModel findReplaceModel) {
		executeState.setFindReplaceModel(findReplaceModel);
	}
}
