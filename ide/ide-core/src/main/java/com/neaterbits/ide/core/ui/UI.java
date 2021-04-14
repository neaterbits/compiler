package com.neaterbits.ide.core.ui;

import com.neaterbits.ide.common.ui.ViewFocusListener;
import com.neaterbits.ide.common.ui.menus.Menus;
import com.neaterbits.ide.core.ui.controller.UIParameters;
import com.neaterbits.ide.core.ui.view.MapMenuItem;
import com.neaterbits.ide.core.ui.view.SystemClipboard;
import com.neaterbits.ide.core.ui.view.UIViewAndSubViews;
import com.neaterbits.util.threads.ForwardResultToCaller;

public interface UI {

	ForwardResultToCaller getIOForwardToCaller();
	
	SystemClipboard getSystemClipboard();
	
	UIViewAndSubViews makeUIView(UIParameters uiParameters, Menus menus, MapMenuItem mapMenuItem);

	void runInitialEvents();

	void main(UIViewAndSubViews mainView);
	
	void addFocusListener(ViewFocusListener focusListener);
	
}
