package dev.nimbler.ide.ui.actions.types.edit;

import dev.nimbler.ide.common.ui.actions.ActionContexts;
import dev.nimbler.ide.ui.actions.ActionApplicableParameters;
import dev.nimbler.ide.ui.actions.ActionExecuteParameters;
import dev.nimbler.ide.ui.actions.CoreAction;

public final class UndoAction extends CoreAction {

	@Override
	public void execute(ActionExecuteParameters parameters) {
		
	}

	@Override
	public boolean isApplicableInContexts(
			ActionApplicableParameters parameters,
			ActionContexts focusedContexts,
			ActionContexts allContexts) {

		return parameters.getUndoRedoBuffer().hasUndoEntries();
	}
}
