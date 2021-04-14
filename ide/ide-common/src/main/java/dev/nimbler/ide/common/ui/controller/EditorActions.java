package dev.nimbler.ide.common.ui.controller;

import dev.nimbler.ide.common.ui.SearchDirection;
import dev.nimbler.ide.common.ui.SearchScope;
import dev.nimbler.ide.util.ui.text.Text;

public interface EditorActions {

	long find(long pos, Text searchText, SearchDirection direction, SearchScope scope, boolean caseSensitive, boolean wrapSearch, boolean wholeWord);
	
	void replace(long pos, long replaceLength, Text replacement);
	
	void selectAll();
	
}
