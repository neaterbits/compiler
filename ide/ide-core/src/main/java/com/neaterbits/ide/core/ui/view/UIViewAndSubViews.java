package com.neaterbits.ide.core.ui.view;

import com.neaterbits.ide.common.ui.view.ViewList;

public interface UIViewAndSubViews extends UIView {

	ProjectView getProjectView();
	
	EditorsView getEditorsView();
	
	ViewList getViewList();

}
