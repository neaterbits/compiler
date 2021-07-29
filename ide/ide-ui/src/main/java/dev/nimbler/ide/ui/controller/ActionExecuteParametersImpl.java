package dev.nimbler.ide.ui.controller;

import java.util.Objects;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.model.clipboard.Clipboard;
import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.ide.common.ui.controller.EditorActions;
import dev.nimbler.ide.common.ui.controller.EditorsActions;
import dev.nimbler.ide.common.ui.view.View;
import dev.nimbler.ide.component.common.IDEComponentsConstAccess;
import dev.nimbler.ide.ui.actions.ActionExecuteParameters;
import dev.nimbler.ide.ui.model.dialogs.FindReplaceDialogModel;
import dev.nimbler.ide.ui.view.UIDialogs;
import dev.nimbler.ide.component.common.action.ActionComponentExeParameters;
import dev.nimbler.ide.component.common.ui.ComponentCompositeContext;
import dev.nimbler.ide.component.common.ui.ComponentDialogContext;

final class ActionExecuteParametersImpl 
	extends ActionComponentExeParameters
    implements ActionExecuteParameters {

	private final ActionExecuteState executeState;
	private final View focusedView;
	private final EditorActions focusedEditor;
	
 	ActionExecuteParametersImpl(
 			ActionExecuteState executeState,
            ComponentDialogContext dialogContext,
            ComponentCompositeContext compositeContext,
 			View focusedView,
			EditorActions focusedEditor) {
	    
	    super(
	            executeState.getCodeAccess(),
	            executeState.getForwardResultToCaller(),
	            executeState.getComponents().getLanguages(),
	            dialogContext,
	            compositeContext,
	            executeState.getComponentIDEAccess());
	    
		Objects.requireNonNull(executeState);
		
		this.executeState = executeState;
		this.focusedView = focusedView;
		this.focusedEditor = focusedEditor;
	}

	@Override
	public IDEComponentsConstAccess getComponents() {
		return executeState.getComponents();
	}

	public SourceFileResourcePath getCurrentSourceFileResourcePath() {
		return executeState.getCurrentSourceFileResourcePath();
	}

	@Override
	public SourceFileModel getSourceFileModel(SourceFileResourcePath sourceFileResourcePath) {
		return executeState.getSourceFileModel(sourceFileResourcePath);
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
