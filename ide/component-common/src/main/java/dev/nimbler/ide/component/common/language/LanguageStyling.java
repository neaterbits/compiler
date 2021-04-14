package dev.nimbler.ide.component.common.language;

import dev.nimbler.ide.common.model.source.SourceFileModel;
import dev.nimbler.ide.util.ui.text.Text;

public interface LanguageStyling {

	// For prefs interface
	Iterable<LanguageStyleable> getStylables();

	// Apply to some text chunk
	Iterable<LanguageStyleOffset> applyStylesToLine(long lineOffset, Text lineText, SourceFileModel sourceFileModel);
	
}
