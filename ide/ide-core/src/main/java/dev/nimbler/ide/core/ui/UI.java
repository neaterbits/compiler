package dev.nimbler.ide.core.ui;

import com.neaterbits.util.threads.ForwardResultToCaller;

import dev.nimbler.ide.common.ui.ViewFocusListener;
import dev.nimbler.ide.common.ui.menus.Menus;
import dev.nimbler.ide.core.ui.controller.UIParameters;
import dev.nimbler.ide.core.ui.view.MapMenuItem;
import dev.nimbler.ide.core.ui.view.SystemClipboard;
import dev.nimbler.ide.core.ui.view.UIViewAndSubViews;

public interface UI {

	ForwardResultToCaller getIOForwardToCaller();
	
	SystemClipboard getSystemClipboard();
	
	UIViewAndSubViews makeUIView(UIParameters uiParameters, Menus menus, MapMenuItem mapMenuItem);

	void runInitialEvents();

	void main(UIViewAndSubViews mainView);
	
	void addFocusListener(ViewFocusListener focusListener);
	
}
