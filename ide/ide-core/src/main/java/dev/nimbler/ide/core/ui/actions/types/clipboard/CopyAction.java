package dev.nimbler.ide.core.ui.actions.types.clipboard;

import dev.nimbler.ide.core.ui.actions.ActionExecuteParameters;

public final class CopyAction extends ToClipboardAction {

	@Override
	public void execute(ActionExecuteParameters parameters) {
		parameters.getFocusedView().copy();
	}
}
