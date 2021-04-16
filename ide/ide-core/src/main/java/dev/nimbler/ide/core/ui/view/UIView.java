package dev.nimbler.ide.core.ui.view;

import dev.nimbler.ide.common.ui.view.KeyEventListener;

public interface UIView extends UIDialogs {
	
	void setWindowTitle(String title);

	void addKeyEventListener(KeyEventListener keyEventListener);

	void minMaxEditors();
}
