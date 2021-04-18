package dev.nimbler.ide.ui.actions.types.edit;

import dev.nimbler.ide.common.ui.SearchDirection;
import dev.nimbler.ide.common.ui.SearchScope;
import dev.nimbler.ide.common.ui.actions.ActionContexts;
import dev.nimbler.ide.ui.actions.ActionApplicableParameters;
import dev.nimbler.ide.ui.actions.ActionExecuteParameters;
import dev.nimbler.ide.ui.actions.CoreAction;
import dev.nimbler.ide.ui.model.dialogs.FindReplaceDialogModel;
import dev.nimbler.ide.util.ui.text.StringText;

abstract class BaseFindAction extends CoreAction {

	abstract SearchDirection getSearchDirection();
	
	@Override
	public final void execute(ActionExecuteParameters parameters) {
		
		final FindReplaceDialogModel dialogModel = parameters.getFindReplaceModel();
		
		parameters.getFocusedEditor().find(
				-1L,
				new StringText(dialogModel.getSearchFor()),
				getSearchDirection(),
				SearchScope.ALL,
				dialogModel.isCaseSensitive(),
				dialogModel.isWrap(),
				dialogModel.isWholeWord());
	}

	@Override
	public final boolean isApplicableInContexts(ActionApplicableParameters parameters, ActionContexts focusedContexts,
			ActionContexts allContexts) {
		
		return parameters.getFindReplaceModel() != null && parameters.getFindReplaceModel().hasSearchText();
	}

}
