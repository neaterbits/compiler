package com.neaterbits.ide.core.ui.controller;

import java.util.Objects;

import com.neaterbits.build.model.BuildRoot;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.ide.common.model.clipboard.Clipboard;
import com.neaterbits.ide.common.model.codemap.CodeMapModel;
import com.neaterbits.ide.common.ui.controller.EditorActions;
import com.neaterbits.ide.common.ui.controller.EditorsActions;
import com.neaterbits.ide.common.ui.view.View;
import com.neaterbits.ide.component.common.ComponentIDEAccess;
import com.neaterbits.ide.component.common.IDEComponentsConstAccess;
import com.neaterbits.ide.core.ui.actions.ActionExecuteParameters;
import com.neaterbits.ide.core.ui.model.dialogs.FindReplaceDialogModel;
import com.neaterbits.ide.core.ui.view.UIDialogs;

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
	public BuildRoot getBuildRoot() {
		return executeState.getBuildRoot();
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
	public CodeMapModel getCodeMap() {
		return executeState.getCodeMap();
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
