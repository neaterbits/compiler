package dev.nimbler.ide.core.ui.controller;

import java.util.Objects;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.model.clipboard.Clipboard;
import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.ide.component.common.action.ActionComponentAppParameters;
import dev.nimbler.ide.component.common.language.Languages;
import dev.nimbler.ide.core.ui.actions.ActionApplicableParameters;
import dev.nimbler.ide.core.ui.model.dialogs.FindReplaceDialogModel;

class ActionApplicableParametersImpl
        extends ActionComponentAppParameters
        implements ActionApplicableParameters {

	private final ActionExecuteState executeState;

	ActionApplicableParametersImpl(ActionExecuteState executeState, Languages languages) {
	    
	    super(executeState.getBuildRoot(), languages);
		
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
