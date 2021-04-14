package com.neaterbits.ide.core.ui.actions.types.clipboard;

import com.neaterbits.ide.common.model.clipboard.ClipboardDataType;
import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.core.ui.actions.ActionApplicableParameters;
import com.neaterbits.ide.core.ui.actions.ActionExecuteParameters;
import com.neaterbits.ide.core.ui.actions.contexts.ClipboardPasteableContext;

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
