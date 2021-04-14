package com.neaterbits.ide.component.common.language;

import com.neaterbits.ide.component.common.IDEComponent;
import com.neaterbits.ide.component.common.language.model.ParseableLanguage;

public interface LanguageComponent extends IDEComponent {

	Iterable<String> getFileSuffixes();
	
	LanguageName getLanguageName();
	
	LanguageStyling getStyling();
	
	ParseableLanguage getParseableLanguage();
	
}
