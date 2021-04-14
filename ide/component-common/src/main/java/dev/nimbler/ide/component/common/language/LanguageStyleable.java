package dev.nimbler.ide.component.common.language;

import dev.nimbler.ide.util.ui.text.styling.TextColor;
import dev.nimbler.ide.util.ui.text.styling.TextStyle;
import dev.nimbler.ide.util.ui.text.styling.TextStyles;

public class LanguageStyleable {

	public static final LanguageStyleable KEYWORD_DEFAULT =
			new LanguageStyleable(new TextColor(0x80, 0x80, 0x50));

	public static final LanguageStyleable LITERAL_DEFAULT =
			new LanguageStyleable(new TextColor(0x50, 0x50, 0xE0));

	public static final LanguageStyleable ENUM_CONSTANT_DEFAULT =
			new LanguageStyleable(new TextColor(0x50, 0x50, 0xE0), new TextStyles(TextStyle.ITALICS));

	private final TextColor defaultColor;
	private final TextStyles textStyles;
	
	public LanguageStyleable(TextColor defaultColor) {
		this(defaultColor, null);
	}
		
	public LanguageStyleable(TextColor defaultColor, TextStyles textStyles) {
		this.defaultColor = defaultColor;
		this.textStyles = textStyles;
	}

	public final TextColor getDefaultColor() {
		return defaultColor;
	}

	public TextStyles getTextStyles() {
		return textStyles;
	}
}
