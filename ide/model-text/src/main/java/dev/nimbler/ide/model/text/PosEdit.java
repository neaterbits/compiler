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

    public static PosEdit merge(TextEdit text1, long startPos1, TextEdit text2, long startPos2) {

        return new PosEdit(startPos1, TextEdit.merge(text1, startPos1, text2, startPos2));
    }
    
    public PosEdit merge(PosEdit other) {
        
        return merge(edit, startPos, other.edit, other.startPos);
    }

    public PosEdit merge(TextEdit other, long otherStartPos) {
        
        return merge(edit, startPos, other, otherStartPos);
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
