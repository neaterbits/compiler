package com.neaterbits.ide.core.ui.actions.types.navigate;

import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.core.ui.actions.ActionApplicableParameters;
import com.neaterbits.ide.core.ui.actions.ActionExecuteParameters;

public final class ShowInProjectsAction extends NavigateAction {

	@Override
	public void execute(ActionExecuteParameters parameters) {
		parameters.getEditorsActions().showCurrentEditedInProjectView();
	}

	@Override
	public boolean isApplicableInContexts(ActionApplicableParameters parameters, ActionContexts focusedViewContexts, ActionContexts allContexts) {
		return true;
	}
}
