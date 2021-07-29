package dev.nimbler.ide.ui.controller;

import java.util.Objects;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.model.clipboard.Clipboard;
import dev.nimbler.ide.ui.actions.ActionApplicableParameters;
import dev.nimbler.ide.ui.model.dialogs.FindReplaceDialogModel;
import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.ide.component.common.action.ActionComponentAppParameters;
import dev.nimbler.ide.component.common.language.Languages;

class ActionApplicableParametersImpl
        extends ActionComponentAppParameters
        implements ActionApplicableParameters {

	private final ActionExecuteState executeState;

	ActionApplicableParametersImpl(ActionExecuteState executeState, Languages languages) {
	    
	    super(executeState.getCodeAccess(), languages);
		
		Objects.requireNonNull(executeState);
		
		this.executeState = executeState;
	}

	@Override
    public final SourceFileResourcePath getCurrentSourceFileResourcePath() {
        return executeState.getCurrentSourceFileResourcePath();
    }

    @Override
    public final SourceFileModel getSourceFileModel(SourceFileResourcePath sourceFileResourcePath) {
        return executeState.getSourceFileModel(sourceFileResourcePath);
    }

    @Override
	public final Clipboard getClipboard() {
		return executeState.getClipboard();
	}

	@Override
	public final UndoRedoBuffer getUndoRedoBuffer() {
		return executeState.getUndoRedoBuffer();
	}

	@Override
	public final FindReplaceDialogModel getFindReplaceModel() {
		return executeState.getFindReplaceModel();
	}
}
