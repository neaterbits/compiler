package dev.nimbler.ide.ui.actions.types.edit;

import dev.nimbler.ide.common.ui.actions.ActionContexts;
import dev.nimbler.ide.ui.actions.ActionApplicableParameters;
import dev.nimbler.ide.ui.actions.ActionExecuteParameters;
import dev.nimbler.ide.ui.actions.CoreAction;
import dev.nimbler.ide.ui.actions.contexts.EditorContext;
import dev.nimbler.ide.ui.model.dialogs.FindReplaceDialogModel;
import dev.nimbler.ide.ui.view.dialogs.FindReplaceDialog;
import dev.nimbler.ide.util.Value;
import dev.nimbler.ide.util.ui.text.StringText;

public final class FindReplaceAction extends CoreAction {

	private FindReplaceDialogModel curDialogModel;
	
	public FindReplaceAction() {
		this.curDialogModel = FindReplaceDialogModel.INITIAL;
	}

	@Override
	public void execute(ActionExecuteParameters parameters) {

		final Value<Long> curFound = new Value<>();
		
		parameters.getUIDialogs().askFindReplace(curDialogModel, dialog -> {

			dialog.addFindTextChangeListener(text -> updateModelAndButtonState(dialog, curFound, parameters));
			dialog.addFindTextEnterKeyListener(() -> {
				
				updateModelAndButtonState(dialog, curFound, parameters);
				
				if (curDialogModel.hasSearchText()) {
					findText(parameters, dialog, curFound);
				}
			});

			dialog.addReplaceWithTextListener(text -> updateModel(dialog, parameters));
			
			dialog.addDirectionForwardListener(() -> updateModel(dialog, parameters));
			dialog.addDirectionBackwardListener(() -> updateModel(dialog, parameters));
			dialog.addScopeAllListener(() -> updateModel(dialog, parameters));
			dialog.addScopeSelectedLinesListener(() -> updateModel(dialog, parameters));
			dialog.addOptionsCaseSensitiveListener(() -> updateModel(dialog, parameters));
			dialog.addOptionsWrapSearchListener(() -> updateModel(dialog, parameters));
			dialog.addOptionsWholeWordListener(() -> updateModel(dialog, parameters));

			updateDialogButtonState(curDialogModel, dialog, curFound);
			
			if (!curDialogModel.getSearchFor().isEmpty()) {
				dialog.setFindTextSelected();
			}
			
			dialog.addFindButtonListener(() -> {
				findText(parameters, dialog, curFound);
			});
			
			dialog.addReplaceFindButtonListener(() -> {
				
				replaceText(curFound.get(), parameters);
				
				findText(parameters, dialog, curFound);
			});
			
			dialog.addReplaceButtonListener(() -> {
				replaceText(curFound.get(), parameters);
				
				curFound.set(null);
				
				updateDialogButtonState(curDialogModel, dialog, curFound);
			});

			dialog.addReplaceAllButtonListener(() -> {
				
				long foundPos;
				
				while (-1 != (foundPos = findText(parameters, dialog, curFound))) {
					replaceText(foundPos, parameters);
				}
			});
		});
	}
	
	private long findText(ActionExecuteParameters parameters, FindReplaceDialog dialog, Value<Long> curFound) {
		
		final long foundPos = parameters.getFocusedEditor().find(
				curFound.get() != null
					? curFound.get() + 1
					: -1L,
				new StringText(curDialogModel.getSearchFor()),
				curDialogModel.getDirection(),
				curDialogModel.getScope(),
				curDialogModel.isCaseSensitive(),
				curDialogModel.isWrap(),
				curDialogModel.isWholeWord());
		
		curFound.set(foundPos != -1 ? foundPos : null);

		updateDialogButtonState(curDialogModel, dialog, curFound);
		
		return foundPos;
	}
	
	private void replaceText(long pos, ActionExecuteParameters parameters) {
		
		parameters.getFocusedEditor().replace(
				pos,
				curDialogModel.getSearchFor().length(),
				new StringText(curDialogModel.getReplaceWith()));
		
	}

	private void updateModel(FindReplaceDialog dialog, ActionExecuteParameters parameters) {

		this.curDialogModel = dialog.getModel();
		
		// For Find Next/Previous
		parameters.storeFindReplaceModel(curDialogModel);
	}

	private void updateModelAndButtonState(FindReplaceDialog dialog, Value<Long> curFound, ActionExecuteParameters parameters) {
		
		updateModel(dialog, parameters);
		
		updateDialogButtonState(curDialogModel, dialog, curFound);
	}
	
	private void updateDialogButtonState(FindReplaceDialogModel model, FindReplaceDialog dialog, Value<Long> curFound) {
		
		final boolean hasSearchText = model.hasSearchText();
		
		dialog.setFindButtonEnabled(hasSearchText);
		
		dialog.setReplaceFindButtonEnabled(curFound.get() != null && hasSearchText);
		dialog.setReplaceButtonEnabled(curFound.get() != null && hasSearchText);
		
		dialog.setReplaceAllButtonEnabled(hasSearchText);
	}

	@Override
	public boolean isApplicableInContexts(ActionApplicableParameters parameters, ActionContexts focusedContexts,
			ActionContexts allContexts) {

		return focusedContexts.hasOfType(EditorContext.class);
	}
}
