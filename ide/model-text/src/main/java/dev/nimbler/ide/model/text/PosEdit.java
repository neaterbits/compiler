package dev.nimbler.ide.model.text;

import java.util.Objects;

import dev.nimbler.ide.util.ui.text.LineDelimiter;
import dev.nimbler.ide.util.ui.text.Text;

public final class PosEdit {

    private final long startPos;
    private final TextEdit edit;
    
    public PosEdit(long startPos, TextEdit edit) {

        if (startPos < 0L) {
            throw new IllegalArgumentException();
        }
        
        Objects.requireNonNull(edit);
        
        this.startPos = startPos;
        this.edit = edit;
    }
    
    public final PosEdit merge(PosEdit other) {
        
        if (startPos + getNewLength() < other.startPos) {
            throw new IllegalArgumentException();
        }
        
        final long totalOldLength = getOldLength() + other.getOldLength();
        final long totalNewLength = getNewLength() + other.getNewLength();
        
        final PosEdit edit;
        
        if (totalOldLength == 0) {
            if (totalNewLength == 0) {
                edit = null;
            }
            else {
                edit = new PosEdit(startPos, new TextAdd(getNewText().merge(other.getNewText())));
            }
        }
        else {
            if (totalNewLength == 0) {
                edit = new PosEdit(
                        startPos,
                        new TextRemove(totalOldLength, getOldText().merge(other.getOldText())));
            }
            else {
                edit = new PosEdit(
                        startPos,
                        new TextReplace(
                            totalOldLength,
                            getOldText().merge(other.getOldText()),
                            getNewText().merge(other.getNewText())));
                        
            }
        }
        
        return edit;
    }

    public long getStartPos() {
        return startPos;
    }

    public TextEdit getTextEdit() {
        return edit;
    }

    public long getOldLength() {
        return edit.getOldLength();
    }

    public Text getOldText() {
        return edit.getOldText();
    }

    public long getNewLength() {
        return edit.getNewLength();
    }
    
    public Text getNewText() {
        return edit.getNewText();
    }

    public long getChangeInNumberOfLines(LineDelimiter lineDelimiter) {
        return edit.getChangeInNumberOfLines(lineDelimiter);
    }
}
