package dev.nimbler.ide.ui.view;

import dev.nimbler.ide.common.ui.view.ViewList;
import dev.nimbler.ide.component.common.ui.ComponentCompositeContext;
import dev.nimbler.ide.component.common.ui.ComponentDialogContext;

public interface UIViewAndSubViews extends UIView {

	ProjectView getProjectView();
	
	EditorsView getEditorsView();
	
	ViewList getViewList();

	ComponentDialogContext getComponentDialogContext();
	
	ComponentCompositeContext getComponentCompositeContext();
}
