package dev.nimbler.ide.util.ui.text;

public interface TextBuilder {

	TextBuilder append(Text text);

	TextBuilder append(String string);

	TextBuilder append(char c);
	
	Text toText();
}
