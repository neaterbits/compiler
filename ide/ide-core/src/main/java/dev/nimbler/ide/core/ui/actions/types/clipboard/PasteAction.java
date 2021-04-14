package dev.nimbler.ide.core.ui.actions.types.clipboard;

import dev.nimbler.ide.common.model.clipboard.ClipboardDataType;
import dev.nimbler.ide.common.ui.actions.ActionContexts;
import dev.nimbler.ide.core.ui.actions.ActionApplicableParameters;
import dev.nimbler.ide.core.ui.actions.ActionExecuteParameters;
import dev.nimbler.ide.core.ui.actions.contexts.ClipboardPasteableContext;

public final class PasteAction extends ClipboardAction {

	@Override
	public void execute(ActionExecuteParameters parameters) {
		
		parameters.getFocusedView().paste();
	}

	@Override
	public boolean isApplicableInContexts(ActionApplicableParameters parameters, ActionContexts focusedViewContexts, ActionContexts allContexts) {
		
		final ClipboardPasteableContext context = focusedViewContexts.getOfType(ClipboardPasteableContext.class);
		
		boolean applicable;
		
		if (context == null) {
			applicable = false;
		}
		else {
			
			applicable = false;
			
			for (ClipboardDataType dataType : context.getPasteableDataTypes()) {
				if (parameters.getClipboard().hasDataType(dataType)) {
					applicable = true;
					break;
				}
			}
		}

		return applicable;
	}
}
