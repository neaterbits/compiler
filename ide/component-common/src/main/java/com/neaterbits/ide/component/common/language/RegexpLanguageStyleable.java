package com.neaterbits.ide.component.common.language;

import java.util.regex.Pattern;

import com.neaterbits.ide.util.ui.text.styling.TextColor;

public class RegexpLanguageStyleable extends LanguageStyleable {

	private final Pattern pattern;

	public RegexpLanguageStyleable(LanguageStyleable styleable, String regexp) {
		this(styleable.getDefaultColor(), regexp);
	}

	public RegexpLanguageStyleable(TextColor defaultColor, String regexp) {
		super(defaultColor);

		this.pattern = Pattern.compile(regexp);
	}

	Pattern getPattern() {
		return pattern;
	}
}
