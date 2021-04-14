package com.neaterbits.ide.core.ui.actions.types.clipboard;

import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.core.ui.actions.ActionApplicableParameters;
import com.neaterbits.ide.core.ui.actions.ActionExecuteParameters;
import com.neaterbits.ide.core.ui.actions.contexts.ClipboardSelectionContext;

public final class CutAction extends ClipboardAction {

	@Override
	public void execute(ActionExecuteParameters parameters) {
		
	}

	@Override
	public boolean isApplicableInContexts(ActionApplicableParameters parameters, ActionContexts focusedViewContexts, ActionContexts allContexts) {
		return focusedViewContexts.hasOfType(ClipboardSelectionContext.class);
	}
}
