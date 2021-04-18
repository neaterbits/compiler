package dev.nimbler.ide.ui.actions.types.edit;

import dev.nimbler.ide.common.ui.actions.ActionContexts;
import dev.nimbler.ide.ui.actions.ActionApplicableParameters;
import dev.nimbler.ide.ui.actions.ActionExecuteParameters;
import dev.nimbler.ide.ui.actions.CoreAction;

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
