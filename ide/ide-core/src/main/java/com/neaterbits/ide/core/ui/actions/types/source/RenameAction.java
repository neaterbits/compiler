package com.neaterbits.ide.core.ui.actions.types.source;

import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.core.ui.actions.ActionApplicableParameters;
import com.neaterbits.ide.core.ui.actions.ActionExecuteParameters;
import com.neaterbits.ide.core.ui.actions.contexts.source.RenameContext;

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
