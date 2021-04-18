package dev.nimbler.ide.ui.view;

public interface UIView extends UIDialogs {
	
	void setWindowTitle(String title);

	void addKeyEventListener(KeyEventListener keyEventListener);

	void minMaxEditors();
}
