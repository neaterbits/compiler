package com.neaterbits.ide.component.common.language;

import com.neaterbits.build.types.resource.SourceFileResourcePath;

public interface Languages {

	LanguageComponent getLanguageComponent(LanguageName languageName);
	
	LanguageName detectLanguage(SourceFileResourcePath sourceFile);
}
