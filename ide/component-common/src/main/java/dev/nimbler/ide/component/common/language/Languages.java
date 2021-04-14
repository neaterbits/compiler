package dev.nimbler.ide.component.common.language;

import dev.nimbler.build.types.resource.SourceFileResourcePath;

public interface Languages {

	LanguageComponent getLanguageComponent(LanguageName languageName);
	
	LanguageName detectLanguage(SourceFileResourcePath sourceFile);
}
