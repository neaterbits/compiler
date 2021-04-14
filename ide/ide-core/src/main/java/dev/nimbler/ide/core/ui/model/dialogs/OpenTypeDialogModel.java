package dev.nimbler.ide.core.ui.model.dialogs;

import dev.nimbler.ide.common.model.codemap.TypeSuggestions;

public interface OpenTypeDialogModel {

	TypeSuggestions getSuggestions(String searchText);
	
}
