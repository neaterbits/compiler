package dev.nimbler.ide.model.text;

import java.util.Objects;

import dev.nimbler.ide.util.ui.text.LineDelimiter;
import dev.nimbler.ide.util.ui.text.Text;

public final class TextReplace extends TextChange {

	private final Text updatedText;
	
	public TextReplace(long replacedLength, Text changedText, Text updatedText) {
		super(replacedLength, changedText);
		
		Objects.requireNonNull(updatedText);
		
		if (updatedText.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		this.updatedText = updatedText;
	}

	@Override
	public Text getNewText() {
		return updatedText;
	}

	@Override
	public long getNewLength() {
		return updatedText.length();
	}

	@Override
	public long getChangeInNumberOfLines(LineDelimiter lineDelimiter) {
		return    updatedText.getNumberOfNewlineChars(lineDelimiter)
				- getOldText().getNumberOfNewlineChars(lineDelimiter);
	}

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(updatedText);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        final TextReplace other = (TextReplace) obj;
        return Objects.equals(updatedText, other.updatedText);
    }
}

