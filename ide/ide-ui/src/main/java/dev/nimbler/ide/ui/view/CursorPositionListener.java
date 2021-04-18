package dev.nimbler.ide.ui.view;

@FunctionalInterface
public interface CursorPositionListener {

	void onCursorPositionChanged(long cursorPos);
}
