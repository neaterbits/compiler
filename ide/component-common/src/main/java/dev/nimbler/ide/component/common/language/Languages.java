package dev.nimbler.ide.component.common.language;

import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.ide.common.codeaccess.types.LanguageName;

public interface Languages {

	LanguageComponent getLanguageComponent(LanguageName languageName);
	
	LanguageName detectLanguage(SourceFileResourcePath sourceFile);
}
