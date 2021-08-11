package dev.nimbler.ide.model.text;

import java.util.Objects;

import dev.nimbler.ide.util.ui.text.LineDelimiter;
import dev.nimbler.ide.util.ui.text.Text;

public final class TextAdd extends TextEdit {

	private final Text text;
	
	public TextAdd(Text text) {

		Objects.requireNonNull(text);
		
		this.text = text;
	}

	public final Text getAddedText() {
		return text;
	}

	@Override
	public Text getOldText() {
		return Text.EMPTY_TEXT;
	}

	@Override
	public long getOldLength() {
		return 0;
	}
	
	@Override
	public Text getNewText() {
		return text;
	}

	@Override
	public long getNewLength() {
		return text.length();
	}

	@Override
	public long getChangeInNumberOfLines(LineDelimiter lineDelimiter) {
		return getChangeInNumberOfLines(text, lineDelimiter);
	}

	@Override
    public int hashCode() {
        return Objects.hash(text);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final TextAdd other = (TextAdd) obj;
        return Objects.equals(text, other.text);
    }

    @Override
	public String toString() {
		return "TextAdd [text=" + text + "]";
	}
}
