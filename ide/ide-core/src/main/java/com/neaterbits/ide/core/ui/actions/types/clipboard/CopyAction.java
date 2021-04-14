package com.neaterbits.ide.core.ui.actions.types.clipboard;

import com.neaterbits.ide.core.ui.actions.ActionExecuteParameters;

public final class CopyAction extends ToClipboardAction {

	@Override
	public void execute(ActionExecuteParameters parameters) {
		parameters.getFocusedView().copy();
	}
}
