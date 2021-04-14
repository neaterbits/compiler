package com.neaterbits.ide.core.ui.actions.types.edit;

import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.core.ui.actions.ActionApplicableParameters;
import com.neaterbits.ide.core.ui.actions.ActionExecuteParameters;
import com.neaterbits.ide.core.ui.actions.CoreAction;
import com.neaterbits.ide.core.ui.actions.contexts.EditorContext;

public final class SelectAllAction extends CoreAction {

	@Override
	public void execute(ActionExecuteParameters parameters) {

		parameters.getFocusedEditor().selectAll();
		
	}

	@Override
	public boolean isApplicableInContexts(ActionApplicableParameters parameters, ActionContexts focusedContexts,
			ActionContexts allContexts) {
	
		return focusedContexts.hasOfType(EditorContext.class);
	}
}
