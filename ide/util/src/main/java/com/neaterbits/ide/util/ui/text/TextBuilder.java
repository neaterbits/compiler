package com.neaterbits.ide.util.ui.text;

public interface TextBuilder {

	TextBuilder append(Text text);
	
	TextBuilder append(char c);
	
	Text toText();
}
