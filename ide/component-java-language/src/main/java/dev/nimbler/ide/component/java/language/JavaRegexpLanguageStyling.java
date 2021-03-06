package dev.nimbler.ide.component.java.language;

import java.util.Arrays;
import java.util.List;

import dev.nimbler.ide.component.common.language.LanguageStyleable;
import dev.nimbler.ide.component.common.language.RegexpLanguageStyleable;
import dev.nimbler.ide.component.common.language.RegexpLanguageStyling;

@Deprecated // Used parsed tokens instead
final class JavaRegexpLanguageStyling extends RegexpLanguageStyling {

	private static List<RegexpLanguageStyleable> STYLEABLES = Arrays.asList(
			
			new RegexpLanguageStyleable(
					LanguageStyleable.KEYWORD_DEFAULT,
					" interface | class | if | for | while | do | throws | throw ")
			
	);
	
	
	JavaRegexpLanguageStyling() {
		super(STYLEABLES);
	}
}
