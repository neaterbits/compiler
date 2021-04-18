package dev.nimbler.ide.ui.actions.types.navigate;

import dev.nimbler.ide.common.ui.actions.ActionContexts;
import dev.nimbler.ide.ui.actions.ActionApplicableParameters;
import dev.nimbler.ide.ui.actions.ActionExecuteParameters;

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
