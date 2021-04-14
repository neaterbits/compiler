package dev.nimbler.ide.core.ui.actions.types.source;

import dev.nimbler.ide.common.ui.actions.ActionContexts;
import dev.nimbler.ide.core.ui.actions.ActionApplicableParameters;
import dev.nimbler.ide.core.ui.actions.ActionExecuteParameters;
import dev.nimbler.ide.core.ui.actions.contexts.source.RenameContext;

public final class RenameAction extends RefactorAction {

	@Override
	public void execute(ActionExecuteParameters parameters) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isApplicableInContexts(ActionApplicableParameters parameters, ActionContexts focusedContexts, ActionContexts allContexts) {
		return focusedContexts.hasOfType(RenameContext.class);
	}
}
