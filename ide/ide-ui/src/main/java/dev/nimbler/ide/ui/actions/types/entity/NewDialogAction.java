package dev.nimbler.ide.ui.actions.types.entity;

import java.util.List;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.ui.actions.ActionContexts;
import dev.nimbler.ide.component.common.instantiation.InstantiationComponentUI;
import dev.nimbler.ide.component.common.instantiation.NewableCategory;
import dev.nimbler.ide.ui.actions.ActionApplicableParameters;
import dev.nimbler.ide.ui.actions.ActionExecuteParameters;
import dev.nimbler.ide.ui.model.dialogs.NewableSelection;

public final class NewDialogAction extends NewAction {

	@Override
	public void execute(ActionExecuteParameters parameters) {
		
		final List<NewableCategory> newables = parameters.getComponents().getNewableCategories();
		
		final NewableSelection newableSelection = parameters.getUIDialogs().askCreateNewable(newables);
		
		System.out.println("NewableSelection: " + newableSelection);
		
		if (newableSelection != null) {
		
			final InstantiationComponentUI componentUI = parameters.getComponents().findInstantiationUIComponent(
					newableSelection.getCategory(),
					newableSelection.getNewable());
			
			final SourceFileResourcePath currentEditedFile = parameters.getCurrentEditedFile();
			
			parameters.getUIDialogs().openNewableDialog(
					componentUI,
					newableSelection.getCategory(),
					newableSelection.getNewable(),
					currentEditedFile != null ? currentEditedFile.getSourceFolderPath() : null,
					currentEditedFile != null ? currentEditedFile.getNamespacePath() : null,
					parameters.getComponentIDEAccess());
		}
	}

	@Override
	public boolean isApplicableInContexts(ActionApplicableParameters parameters, ActionContexts focusedViewContexts, ActionContexts allContexts) {
		return true;
	}
}
