package dev.nimbler.ide.model.text;

import java.util.Objects;

import dev.nimbler.ide.util.ui.text.Text;

public abstract class TextChange extends TextEdit {

	private final long length;
	private final Text changedText;

	public TextChange(long length, Text changedText) {
		
		Objects.requireNonNull(changedText);
	
		this.length = length;
		this.changedText = changedText;
	}

	@Override
	public final long getOldLength() {
		return length;
	}

	@Override
	public final Text getOldText() {
		return changedText;
	}

    @Override
    public int hashCode() {
        return Objects.hash(changedText, length);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final TextChange other = (TextChange) obj;
        return Objects.equals(changedText, other.changedText) && length == other.length;
    }
}
