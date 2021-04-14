package dev.nimbler.ide.core.ui.view;

import dev.nimbler.ide.util.ui.text.Text;

@FunctionalInterface
public interface TextEditorChangeListener {

	void onTextChange(long start, long length, Text updatedText);
	
}
