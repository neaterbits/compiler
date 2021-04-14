package com.neaterbits.ide.component.common.language;

import com.neaterbits.ide.common.model.source.SourceFileModel;
import com.neaterbits.ide.util.ui.text.Text;

public interface LanguageStyling {

	// For prefs interface
	Iterable<LanguageStyleable> getStylables();

	// Apply to some text chunk
	Iterable<LanguageStyleOffset> applyStylesToLine(long lineOffset, Text lineText, SourceFileModel sourceFileModel);
	
}
