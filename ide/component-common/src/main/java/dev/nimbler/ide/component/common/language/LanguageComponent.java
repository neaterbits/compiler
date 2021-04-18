package dev.nimbler.ide.component.common.language;

import dev.nimbler.ide.common.codeaccess.types.LanguageName;
import dev.nimbler.ide.component.common.IDEComponent;
import dev.nimbler.ide.component.common.language.model.ParseableLanguage;

public interface LanguageComponent extends IDEComponent {

	Iterable<String> getFileSuffixes();
	
	LanguageName getLanguageName();
	
	LanguageStyling getStyling();
	
	ParseableLanguage getParseableLanguage();
	
}
