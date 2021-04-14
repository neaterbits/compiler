package dev.nimbler.ide.core.ui.controller;

import java.util.Objects;

import dev.nimbler.ide.common.model.clipboard.Clipboard;
import dev.nimbler.ide.core.ui.actions.ActionApplicableParameters;
import dev.nimbler.ide.core.ui.model.dialogs.FindReplaceDialogModel;

final class ActionApplicableParametersImpl implements ActionApplicableParameters {

	private final ActionExecuteState executeState;

	ActionApplicableParametersImpl(ActionExecuteState executeState) {
		
		Objects.requireNonNull(executeState);
		
		this.executeState = executeState;
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
	public FindReplaceDialogModel getFindReplaceModel() {
		return executeState.getFindReplaceModel();
	}
}
