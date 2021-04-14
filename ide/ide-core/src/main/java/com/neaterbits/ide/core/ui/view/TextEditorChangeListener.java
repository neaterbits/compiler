package com.neaterbits.ide.core.ui.view;

import com.neaterbits.ide.util.ui.text.Text;

@FunctionalInterface
public interface TextEditorChangeListener {

	void onTextChange(long start, long length, Text updatedText);
	
}
