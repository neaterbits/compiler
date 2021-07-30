package dev.nimbler.ide.model.text;

import dev.nimbler.ide.util.ui.text.Text;

/**
 * Helper class for merge of {@link TextEdit} elements
 * 
 */
class TextEditMerge {

    static TextEdit merge(TextEdit text1, long startPos1, TextEdit text2, long startPos2) {

        TextEdit merged = null;

        // text1 must be before or at text2
        if (startPos2 < startPos1) {
            throw new IllegalArgumentException();
        }

        if (startPos1 + text1.getNewLength() < startPos2) {
            throw new IllegalArgumentException();
        }
        
        if (text1 instanceof TextAdd) {
            
            if (text2 instanceof TextAdd) {

                merged = mergeAddWithAdd((TextAdd)text1, startPos1, (TextAdd)text2, startPos2);
            }
            else if (text2 instanceof TextReplace) {

                merged = mergeAddWithReplace((TextAdd)text1, startPos1, (TextReplace)text2, startPos2);
            }
            else if (text2 instanceof TextRemove) {
                
                // If within earlier TextAdd, then just remove from earlier TextAdd
                merged = mergeAddWithRemove((TextAdd)text1, startPos1, (TextRemove)text2, startPos2);
            }
            else {
                throw new IllegalStateException();
            }
        }
        else if (text1 instanceof TextReplace) {
            
            if (text2 instanceof TextAdd) {
                
            }
            else if (text2 instanceof TextReplace) {
                
            }
            else if (text2 instanceof TextRemove) {
                
            }
            else {
                throw new IllegalStateException();
            }
        }
        else if (text1 instanceof TextRemove) {
            
            if (text2 instanceof TextAdd) {
                merged = mergeRemoveWithAdd((TextRemove)text1, startPos1, (TextAdd)text2, startPos2);
            }
            else if (text2 instanceof TextReplace) {
                
            }
            else if (text2 instanceof TextRemove) {
                
            }
            else {
                throw new IllegalStateException();
            }
        }
        else {
            throw new IllegalStateException();
        }
        
        return merged;
    }
    
    private static TextEdit mergeAddWithAdd(TextAdd text1, long startPos1, TextAdd text2, long startPos2) {

        final TextEdit merged;
        
        if (startPos1 + text1.getNewLength() < startPos2) {
            throw new IllegalArgumentException();
        }
        else if (startPos1 + text1.getNewLength() == startPos2) {
            // text1, then text2
            merged = new TextAdd(startPos1, text1.getNewText().merge(text2.getNewText()));
        }
        else if (startPos1 == startPos2) {
            merged = new TextAdd(startPos1, text2.getNewText().merge(text1.getNewText()));
        }
        else {
            final long index = startPos2 - startPos1;

            merged = new TextAdd(
                        startPos1,
                        text1.getNewText().substring(0L, index)
                            .merge(text2.getNewText())
                            .merge(text1.getNewText().substring(index)));
        }

        return merged;
    }

    private static TextEdit mergeAddWithReplace(TextAdd text1, long startPos1, TextReplace text2, long startPos2) {

        final TextEdit merged;
        
        final long text1EndPos = startPos1 + text1.getNewLength();
        final long text2ReplacedEndPos = startPos2 + text2.getOldLength();
        
        if (text1EndPos < startPos2) {
            throw new IllegalArgumentException();
        }
        else if (text1EndPos >= text2ReplacedEndPos) {
            // Only replacing in earlier added text so can swap out for a new TextAdd
            // by replacing in the existing TextAdd
            
            if (startPos1 == startPos2) {
                
                merged = new TextAdd(
                        startPos1,
                        text2.getNewText()
                            .merge(
                                    text1.getNewText().substring(
                                            text2.getOldLength(),
                                            text1.getNewLength() - text2.getOldLength() + 1)));
            }
            else if (text1EndPos == text2ReplacedEndPos) {

                merged = new TextAdd(
                        startPos1,
                        text1.getNewText().substring(0, text1.getNewLength() - text2.getOldLength())
                            .merge(text2.getNewText()));
                
            }
            else {
                
                final long index = startPos2 - startPos1;

                merged = new TextAdd(
                        startPos1,
                        text1.getNewText().substring(0, index)
                            .merge(text2.getNewText())
                            .merge(text1.getNewText().substring(index + text2.getOldLength())));
                
            }
        }
        else {
            // Must change to a replace when replace old-text spans beyond added text
            final Text replaced;
            final Text updated;
            
            if (startPos1 == startPos2) {
                replaced = text2.getOldText();
                updated = text2.getNewText();
            }
            else {
                
                replaced = text1.getNewText().substring(0, text1.getNewLength() - text2.getOldLength() + 1)
                            .merge(text2.getOldText());
                
                updated = text1.getNewText().substring(0, text1.getNewLength() - text2.getOldLength() + 1)
                        .merge(text2.getNewText());
                
            }
            
            merged = new TextReplace(
                    startPos1,
                    replaced.length(),
                    replaced,
                    updated);
        }
        
        return merged;
    }

    private static TextEdit mergeAddWithRemove(TextAdd text1, long startPos1, TextRemove text2, long startPos2) {

        if (startPos2 >= startPos1 + text1.getNewLength()) {
            throw new IllegalArgumentException();
        }
        
        final TextEdit merged;

        final long text1EndPos = startPos1 + text1.getNewLength();
        final long text2RemovedEndPos = startPos2 + text2.getOldLength();

        if (startPos1 == startPos2) {
            
            if (text1.getNewLength() == text2.getOldLength()) {
                merged = null;
            }
            else if (text1.getNewLength() > text2.getOldLength()) {
                merged = new TextAdd(startPos1, text1.getNewText().substring(text2.getOldLength()));
            }
            else {
                // text1.getNewLength() < text2.getOldLength()
                final long removedCount = text2.getOldLength() - text1.getNewLength();
                final Text replaced = text2.getOldText().substring(text1.getNewLength());
                
                merged = new TextRemove(
                        startPos2,
                        removedCount,
                        replaced);
            }
        }
        else if (text1EndPos >= text2RemovedEndPos) {
            
            if (text1EndPos == text2RemovedEndPos) {

                merged = new TextAdd(
                        startPos1,
                        text1.getNewText().substring(0, text1.getNewLength() - text2.getOldLength()));
            }
            else if (text1EndPos > text2RemovedEndPos) {

                
                final long index = startPos2 - startPos1;

                merged = new TextAdd(
                        startPos1,
                        text1.getNewText().substring(0, index)
                            .merge(text1.getNewText().substring(index + text2.getOldLength())));
                
            }
            else {
                throw new IllegalStateException();
            }
        }
        else {
            // Must change to a replace when replace old-text spans beyond added text
            
            final Text replaced;
            final Text remaining;
            
            final long index = startPos2 - startPos1;

            replaced = text2.getOldText().substring(text1.getNewLength() - index);
            remaining = text1.getNewText().substring(0, index);

            merged = new TextReplace(
                    startPos1,
                    replaced.length(),
                    replaced,
                    remaining);
        }
        
        return merged;
    }
    
    private static TextEdit mergeRemoveWithAdd(TextRemove text1, long startPos1, TextAdd text2, long startPos2) {

        if (startPos1 != startPos2) {
            throw new IllegalArgumentException();
        }
        
        final TextEdit merged = new TextReplace(
                startPos1,
                text1.getOldLength(),
                text1.getOldText(),
                text2.getNewText());

        return merged;
    }
}
