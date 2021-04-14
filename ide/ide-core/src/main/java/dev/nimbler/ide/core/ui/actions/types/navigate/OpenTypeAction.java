package dev.nimbler.ide.core.ui.actions.types.navigate;

import dev.nimbler.ide.common.model.codemap.TypeSuggestion;
import dev.nimbler.ide.common.model.codemap.TypeSuggestions;
import dev.nimbler.ide.common.ui.actions.ActionContexts;
import dev.nimbler.ide.core.ui.actions.ActionApplicableParameters;
import dev.nimbler.ide.core.ui.actions.ActionExecuteParameters;
import dev.nimbler.ide.core.ui.model.dialogs.OpenTypeDialogModel;

public class OpenTypeAction extends NavigateAction {

	@Override
	public void execute(ActionExecuteParameters parameters) {
		final TypeSuggestion typeSuggestion = parameters.getUIDialogs().askOpenType(new OpenTypeDialogModel() {
				
				@Override
				public TypeSuggestions getSuggestions(String searchText) {
					return parameters.getCodeMap().findSuggestions(searchText, true);
				}
			});
		
		if (typeSuggestion != null) {
			parameters.getEditorsActions().openSourceFileForEditing(typeSuggestion.getSourceFile());
		}
	}

	@Override
	public boolean isApplicableInContexts(ActionApplicableParameters parameters, ActionContexts focusedViewContexts, ActionContexts allContexts) {
		return true;
	}

}
