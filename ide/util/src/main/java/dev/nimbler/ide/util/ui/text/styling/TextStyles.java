package dev.nimbler.ide.util.ui.text.styling;

import org.jutils.EnumMask;

public class TextStyles extends EnumMask<TextStyle> {
	
	public TextStyles(TextStyle ... styles) {
		super(TextStyle.class, styles);
	}
}
