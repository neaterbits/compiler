package dev.nimbler.ide.component.console.output.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

final class ListConsoleTexts extends ConsoleTexts {

    private final List<ConsoleText> texts;

    ListConsoleTexts() {
        this.texts = new ArrayList<>();
    }

    @Override
    ConsoleText getLast() {

        return texts.isEmpty() ? null : texts.get(texts.size() - 1);
    }

    @Override
    ConsoleText getFirst() {

        return texts.isEmpty() ? null : texts.get(0);
    }

    @Override
    ConsoleText getLast(int count) {

        return texts.get(texts.size() - 1 - count);
    }

    @Override
    void replaceLast(ConsoleText other) {

        Objects.requireNonNull(other);
        
        texts.set(texts.size() - 1, other);
    }

    @Override
    void removeFirst(int count) {
        
        if (count < 0) {
            throw new IllegalArgumentException();
        }
        else if (count > texts.size()) {
            throw new IllegalArgumentException();
        }
        else if (count == texts.size()) {
            texts.clear();
        }
        else {
            final ConsoleText upTo = texts.get(count + 1);
    
            texts.removeIf(o -> o != upTo || o .getOffsetFromStart() > upTo.getOffsetFromStart());
        }
    }

    @Override
    boolean isEmpty() {
        
        return texts.isEmpty();
    }

    @Override
    int size() {

        return texts.size();
    }

    @Override
    void add(ConsoleText text) {
        
        Objects.requireNonNull(text);
        
        if (isEmpty()) {
            if (text.getOffsetFromStart() != 0L) {
                throw new IllegalArgumentException();
            }
        }
        else {
            final ConsoleText last = getLast();

            if (text.getOffsetFromStart() != last.getOffsetFromStart() + last.getCharCount()) {
                throw new IllegalArgumentException();
            }
        }
     
        texts.add(text);
    }

    List<ConsoleText> getTextsInRange(long startPos, long length) {

        final List<ConsoleText> results;
        
        if (startPos < 0) {
            throw new IllegalArgumentException();
        }
        
        if (length < 0) {
            throw new IllegalArgumentException();
        }
        else if (length == 0L) {
            results = Collections.emptyList();
        }
        else {
            results = new ArrayList<>();
        
            for (ConsoleText text : texts) {
                
                final long charCount = text.getCharCount();
                
                if (
                        (    startPos >= text.getOffsetFromStart()
                          && startPos < text.getOffsetFromStart() + charCount)
                        
                        ||
                        
                        (    startPos < text.getOffsetFromStart()
                          && startPos + length > text.getOffsetFromStart())) {
                            
    
                    results.add(text);
                }
            }
        }

        return results;
    }
}
