package dev.nimbler.ide.core.ui.actions.types.clipboard;

import dev.nimbler.ide.common.ui.actions.ActionContexts;
import dev.nimbler.ide.core.ui.actions.ActionApplicableParameters;
import dev.nimbler.ide.core.ui.actions.contexts.ClipboardSelectionContext;

public abstract class ToClipboardAction extends ClipboardAction {

	@Override
	public final boolean isApplicableInContexts(ActionApplicableParameters parameters, ActionContexts focusedViewContexts, ActionContexts allContexts) {
		return focusedViewContexts.hasOfType(ClipboardSelectionContext.class);
	}

}
