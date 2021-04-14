package dev.nimbler.ide.core.ui.view;

import dev.nimbler.ide.common.ui.view.ViewList;

public interface UIViewAndSubViews extends UIView {

	ProjectView getProjectView();
	
	EditorsView getEditorsView();
	
	ViewList getViewList();

}
