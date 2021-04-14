package com.neaterbits.ide.core.ui.actions.types.edit;

import com.neaterbits.ide.common.ui.SearchDirection;
import com.neaterbits.ide.common.ui.SearchScope;
import com.neaterbits.ide.common.ui.actions.ActionContexts;
import com.neaterbits.ide.core.ui.actions.ActionApplicableParameters;
import com.neaterbits.ide.core.ui.actions.ActionExecuteParameters;
import com.neaterbits.ide.core.ui.actions.CoreAction;
import com.neaterbits.ide.core.ui.model.dialogs.FindReplaceDialogModel;
import com.neaterbits.ide.util.ui.text.StringText;

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
