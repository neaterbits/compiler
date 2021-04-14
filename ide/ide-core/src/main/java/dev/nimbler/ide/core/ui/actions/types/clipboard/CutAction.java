package dev.nimbler.ide.core.ui.actions.types.clipboard;

import dev.nimbler.ide.common.ui.actions.ActionContexts;
import dev.nimbler.ide.core.ui.actions.ActionApplicableParameters;
import dev.nimbler.ide.core.ui.actions.ActionExecuteParameters;
import dev.nimbler.ide.core.ui.actions.contexts.ClipboardSelectionContext;

public final class CutAction extends ClipboardAction {

	@Override
	public void execute(ActionExecuteParameters parameters) {
		
	}

	@Override
	public boolean isApplicableInContexts(ActionApplicableParameters parameters, ActionContexts focusedViewContexts, ActionContexts allContexts) {
		return focusedViewContexts.hasOfType(ClipboardSelectionContext.class);
	}
}
