package com.neaterbits.ide.util.ui.text.styling;

import com.neaterbits.compiler.util.EnumMask;

public class TextStyles extends EnumMask<TextStyle> {
	
	public TextStyles(TextStyle ... styles) {
		super(TextStyle.class, styles);
	}
}
