package dev.nimbler.ide.model.text;

import java.util.Objects;

import dev.nimbler.ide.util.ui.text.StringText;
import dev.nimbler.ide.util.ui.text.Text;

public abstract class BaseTextEditTest {

    public static final class Edit<T extends TextEdit> {

        private final long startPos;
        private final T edit;
        
        private Edit(long startPos, T edit) {

            Objects.requireNonNull(edit);
            
            this.startPos = startPos;
            this.edit = edit;
        }

        long getStartPos() {
            return startPos;
        }

        public T getEdit() {
            return edit;
        }
        
        public PosEdit toPosEdit() {
            return new PosEdit(startPos, edit);
        }
    }

    public static Edit<TextAdd> add(long startPos, String text) {
        
        return new Edit<>(
                startPos,
                new TextAdd(new StringText(text)));
    }

    public static Edit<TextReplace> replace(long startPos, String oldText, String newText) {
        
        return replace(startPos, oldText.length(), oldText, newText);
    }
    
    private static Edit<TextReplace> replace(long startPos, long oldTextLength, String oldText, String newText) {

        return new Edit<>(
                startPos,
                new TextReplace(
                        oldTextLength,
                        new StringText(oldText),
                        new StringText(newText)));
    }

    public static Edit<TextRemove> remove(long startPos, String oldText) {
        
        return remove(startPos, oldText.length(), oldText);
    }

    private static Edit<TextRemove> remove(long startPos, long oldTextLength, String oldText) {
        
        return new Edit<TextRemove>(
                startPos,
                new TextRemove(
                        oldTextLength,
                        new StringText(oldText)));
                
    }

    public static Text text(String string) {
        return new StringText(string);
    }
}
