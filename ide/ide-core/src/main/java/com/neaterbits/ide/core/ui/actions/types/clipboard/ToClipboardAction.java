package com.neaterbits.ide.core.ui.actions.types.clipboard;

import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.core.ui.actions.ActionApplicableParameters;
import com.neaterbits.ide.core.ui.actions.contexts.ClipboardSelectionContext;

public abstract class ToClipboardAction extends ClipboardAction {

	@Override
	public final boolean isApplicableInContexts(ActionApplicableParameters parameters, ActionContexts focusedViewContexts, ActionContexts allContexts) {
		return focusedViewContexts.hasOfType(ClipboardSelectionContext.class);
	}

}
