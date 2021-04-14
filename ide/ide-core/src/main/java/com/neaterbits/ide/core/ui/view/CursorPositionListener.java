package com.neaterbits.ide.core.ui.view;

@FunctionalInterface
public interface CursorPositionListener {

	void onCursorPositionChanged(long cursorPos);
}
