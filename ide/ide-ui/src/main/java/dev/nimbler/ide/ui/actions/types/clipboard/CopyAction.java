package dev.nimbler.ide.ui.actions.types.clipboard;

import dev.nimbler.ide.ui.actions.ActionExecuteParameters;

public final class CopyAction extends ToClipboardAction {

	@Override
	public void execute(ActionExecuteParameters parameters) {
		parameters.getFocusedView().copy();
	}
}
