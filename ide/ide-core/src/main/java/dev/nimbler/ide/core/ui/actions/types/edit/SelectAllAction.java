package dev.nimbler.ide.core.ui.actions.types.edit;

import dev.nimbler.ide.common.ui.actions.ActionContexts;
import dev.nimbler.ide.core.ui.actions.ActionApplicableParameters;
import dev.nimbler.ide.core.ui.actions.ActionExecuteParameters;
import dev.nimbler.ide.core.ui.actions.CoreAction;
import dev.nimbler.ide.core.ui.actions.contexts.EditorContext;

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
