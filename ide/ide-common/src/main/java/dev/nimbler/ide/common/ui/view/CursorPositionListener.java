package dev.nimbler.ide.common.ui.view;

@FunctionalInterface
public interface CursorPositionListener {

	void onCursorPositionChanged(long cursorPos);
}
