package com.neaterbits.ide.component.java.language;

import java.util.Arrays;
import java.util.List;

import com.neaterbits.ide.component.common.language.LanguageStyleable;
import com.neaterbits.ide.component.common.language.RegexpLanguageStyleable;
import com.neaterbits.ide.component.common.language.RegexpLanguageStyling;

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
