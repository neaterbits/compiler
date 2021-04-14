package com.neaterbits.ide.core.ui.actions.types.edit;

import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.core.ui.actions.ActionApplicableParameters;
import com.neaterbits.ide.core.ui.actions.ActionExecuteParameters;
import com.neaterbits.ide.core.ui.actions.CoreAction;

public class MinMaxEditorsAction extends CoreAction {

	@Override
	public void execute(ActionExecuteParameters parameters) {

		parameters.getEditorsActions().minMaxEditors();
	}

	@Override
	public boolean isApplicableInContexts(ActionApplicableParameters parameters, ActionContexts focusedViewContexts, ActionContexts allContexts) {
		return true;
	}
}
