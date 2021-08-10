package dev.nimbler.ide.model.text;

import java.util.Objects;

import dev.nimbler.ide.util.ui.text.Text;

/**
 * Helper class for merge of {@link TextEdit} elements
 * 
 */
class TextEditMergeHelper {

    /**
     * Whether two edits can be merged, according to their start positions
     * 
     * @param text1 a {@link TextEdit} to merge
     * @param startPos1 position of {@link TextEdit} text1 to merge in complete text
     * @param text2 a {@link TextEdit} to merge
     * @param startPos2 position of {@link TextEdit} text2 to merge in complete text
     * 
     * @return true if one can merge with {@link TextEditMergeHelper#merge}, false otherwise
     *              though texts may be mergeable in the opposite order
     */
    static boolean isMergeable(TextEdit text1, long startPos1, TextEdit text2, long startPos2) {
        
        Objects.requireNonNull(text1);
        Objects.requireNonNull(text2);

        final boolean isMergeable;
        
        if (startPos1 < 0L) {
            throw new IllegalArgumentException();
        }
        else if (startPos2 < 0L) {
            throw new IllegalArgumentException();
        }
        else if (startPos2 < startPos1) {
            // For ease of implementation of merge(), only merge when startPos1 <= startPos2
            isMergeable = false;
        }
        else if (text1 instanceof TextAdd) {
            
            // Add can be merged with
            if (text2 instanceof TextAdd) {

                // ... TextAdd if text2 overlaps or right after text1
                final long text1EndIndex = startPos1 + text1.getNewLength();
                
                isMergeable = startPos2 <= text1EndIndex;
            }
            else if (text2 instanceof TextReplace) {
                
                // ... TextReplace if text2 overlaps so that can replace at least one character
                final long text1EndIndex = startPos1 + text1.getNewLength();
                
                isMergeable = startPos2 < text1EndIndex;
            }
            else if (text2 instanceof TextRemove) {
                
                // ... TextReplace if text2 overlaps so that can remove at least one character
                final long text1EndIndex = startPos1 + text1.getNewLength();
                
                isMergeable = startPos2 < text1EndIndex;
            }
            else {
                throw new IllegalStateException();
            }
        }
        else if (text1 instanceof TextReplace) {
            
            // Replace can be merged with
            if (text2 instanceof TextAdd) {

                // ... TextAdd if text2 overlaps or right after text1
                final long text1EndIndex = startPos1 + text1.getNewLength();
                
                isMergeable = startPos2 <= text1EndIndex;
            }
            else if (text2 instanceof TextReplace) {
                
                // ... TextReplace if text2 overlaps so that can replace at least one character
                final long text1EndIndex = startPos1 + text1.getNewLength();
                
                isMergeable = startPos2 < text1EndIndex;
            }
            else if (text2 instanceof TextRemove) {
                
                // ... TextReplace if text2 overlaps so that can remove at least one character
                final long text1EndIndex = startPos1 + text1.getNewLength();
                
                isMergeable = startPos2 < text1EndIndex;
            }
            else {
                throw new IllegalStateException();
            }
        }
        else if (text1 instanceof TextRemove) {
            
            // Remove can be merged only if at same startPos
            // e.g. initial text "012345", we remove "12" at position 1 so we have "0[12]345"
            // Any operation that can be merged must occur at pos 1, i.e. at '3'
            isMergeable = startPos1 == startPos2;
        }
        else {
            throw new IllegalStateException();
        }
        
        return isMergeable;
    }

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
                merged = mergeReplaceWithAdd((TextReplace)text1, startPos1, (TextAdd)text2, startPos2);
            }
            else if (text2 instanceof TextReplace) {
                merged = mergeReplaceWithReplace((TextReplace)text1, startPos1, (TextReplace)text2, startPos2);
            }
            else if (text2 instanceof TextRemove) {
                merged = mergeReplaceWithRemove((TextReplace)text1, startPos1, (TextRemove)text2, startPos2);
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
                merged = mergeRemoveWithReplace((TextRemove)text1, startPos1, (TextReplace)text2, startPos2);
            }
            else if (text2 instanceof TextRemove) {
                merged = mergeRemoveWithRemove((TextRemove)text1, startPos1, (TextRemove)text2, startPos2);
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
            merged = new TextAdd(text1.getNewText().merge(text2.getNewText()));
        }
        else if (startPos1 == startPos2) {
            merged = new TextAdd(text2.getNewText().merge(text1.getNewText()));
        }
        else {
            final long index = startPos2 - startPos1;

            merged = new TextAdd(
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
                        text2.getNewText()
                            .merge(
                                    text1.getNewText().substring(
                                            text2.getOldLength(),
                                            text1.getNewLength() - text2.getOldLength() + 1)));
            }
            else if (text1EndPos == text2ReplacedEndPos) {

                merged = new TextAdd(
                        text1.getNewText().substring(0, text1.getNewLength() - text2.getOldLength())
                            .merge(text2.getNewText()));
                
            }
            else {
                
                final long index = startPos2 - startPos1;

                merged = new TextAdd(
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
                merged = new TextAdd(text1.getNewText().substring(text2.getOldLength()));
            }
            else {
                // text1.getNewLength() < text2.getOldLength()
                final long removedCount = text2.getOldLength() - text1.getNewLength();
                final Text replaced = text2.getOldText().substring(text1.getNewLength());
                
                merged = new TextRemove(
                        removedCount,
                        replaced);
            }
        }
        else if (text1EndPos >= text2RemovedEndPos) {
            
            if (text1EndPos == text2RemovedEndPos) {

                merged = new TextAdd(
                        text1.getNewText().substring(0, text1.getNewLength() - text2.getOldLength()));
            }
            else if (text1EndPos > text2RemovedEndPos) {

                
                final long index = startPos2 - startPos1;

                merged = new TextAdd(
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
                    replaced.length(),
                    replaced,
                    remaining);
        }
        
        return merged;
    }

    private static TextReplace mergeReplaceWithAdd(TextReplace text1, long startPos1, TextAdd text2, long startPos2) {
        
        final TextReplace merged;
        
        final long replaceEndIndex = startPos1 + text1.getNewLength();
        
        if (startPos2 < startPos1) {
            throw new IllegalArgumentException();
        }
        else if (startPos1 == startPos2) {
            
            // Replace at the same position, added text is prepended
            merged = new TextReplace(
                    text1.getOldLength(),
                    text1.getOldText(),
                    text2.getNewText().merge(text1.getNewText()));
        }
        else if (startPos2 == replaceEndIndex) {
            
            // Add text right after merged
            merged = new TextReplace(
                    text1.getOldLength(),
                    text1.getOldText(),
                    text1.getNewText().merge(text2.getNewText()));
        }
        else if (startPos2 > replaceEndIndex) {
            throw new IllegalArgumentException();
        }
        else {
            // added text merged in the middle of replace text
            merged = new TextReplace(
                    text1.getOldLength(),
                    text1.getOldText(),
                    text1.getNewText().substring(0, startPos2 - startPos1)
                        .merge(text2.getNewText())
                        .merge(text1.getNewText().substring(startPos2 - startPos1)));
        }

        return merged;
    } 

    private static TextReplace mergeReplaceWithReplace(TextReplace text1, long startPos1, TextReplace text2, long startPos2) {
        
        final TextReplace merged;

        final long replace1EndIndex = startPos1 + text1.getNewLength();
        
        if (startPos2 < startPos1) {
            throw new IllegalArgumentException();
        }
        else if (startPos1 == startPos2) {
            
            // e.g. text1 replace to "123", then text 2 replacing "12"
            if (text1.getNewLength() >= text2.getOldLength()) {

                if (!text1.getNewText().startsWith(text2.getOldText())) {
                    throw new IllegalArgumentException();
                }

                merged = new TextReplace(
                        text1.getOldLength(),
                        text1.getOldText(),
                        text2.getNewText()
                            .merge(text1.getNewText().substring(text2.getOldLength())));
            }
            else {
                // e.g. text1 replace to "12", then text 2 replacing "123"

                if (!text2.getOldText().startsWith(text1.getNewText())) {
                    throw new IllegalArgumentException();
                }

                merged = new TextReplace(
                        text2.getOldLength(),
                        text2.getOldText(),
                        text2.getNewText());
                }
        }
        else if (startPos2 >= replace1EndIndex) {
            throw new IllegalArgumentException();
        }
        else {
            // startPos1 < startPos2 < replace1EndIndex 

            final long replace2EndIndex = startPos2 + text2.getNewLength();
            
            final long initialIndex = startPos2 - startPos1;
            
            if (replace1EndIndex >= replace2EndIndex) {
                // text1 new length > text2 new length
                if (!text1.getNewText().substringMatches(initialIndex, text2.getOldText())) {
                    throw new IllegalArgumentException();
                }

                merged = new TextReplace(
                        text1.getOldLength(),
                        text1.getOldText(),
                        text1.getNewText().substring(0, initialIndex)
                            .merge(text2.getNewText())
                            .merge(text1.getNewText().substring(initialIndex + text2.getOldLength())));
            }
            else {
                // replace1EndIndex < replace2EndIndex

                // verify that text2.getOldText() matches text1.getNewText()
                if (!text1.getNewText()
                        .substringMatches(
                                initialIndex,
                                text2.getOldText(),
                                text1.getNewLength() - initialIndex)) {
                    
                    
                    throw new IllegalArgumentException();
                }
                
                final long text2AdditionalOldLength = replace2EndIndex - replace1EndIndex;
                
                merged = new TextReplace(
                        text1.getOldLength() + text2AdditionalOldLength,
                        text1.getOldText().merge(text2.getOldText().trailing(text2AdditionalOldLength)),
                        text1.getNewText().substring(0, initialIndex)
                            .merge(text2.getNewText()));
            }
        }

        return merged;
    }

    private static TextEdit mergeReplaceWithRemove(TextReplace text1, long startPos1, TextRemove text2, long startPos2) {
        
        final TextEdit merged;
        
        final long replaceEndIndex = startPos1 + text1.getNewLength();
        final long removeEndIndex = startPos2 + text2.getOldLength();
        
        if (startPos2 < startPos1) {
            throw new IllegalArgumentException();
        }
        else if (startPos1 == startPos2) {
            
            if (text1.getNewLength() <= text2.getOldLength()) {
                if (!text2.getOldText().startsWith(text1.getNewText())) {
                    throw new IllegalArgumentException();
                }
                
                // More removed than replaced, must add to old removed length
                final Text removed = text1.getOldText()
                        .merge(text2.getOldText().substring(text1.getNewLength()));

                merged = new TextRemove(removed.length(), removed);
            }
            else if (text2.getOldLength() <= text1.getNewLength()) {
                
                if (!text1.getNewText().startsWith(text2.getOldText())) {
                    throw new IllegalArgumentException();
                }
                
                final Text newText = text1.getNewText().substring(text2.getOldLength());
            
                merged = new TextReplace(
                        text1.getOldLength(),
                        text1.getOldText(),
                        newText);
            }
            else {
                merged = null;
            }
        }
        else if (startPos2 >= replaceEndIndex) {
            // Remove after end of replace
            throw new IllegalArgumentException();
        }
        else {
            // startPos2 within replaced so removing parts of replaced string
            if (replaceEndIndex == removeEndIndex) {

                if (!text1.getNewText().endsWith(text2.getOldText())) {
                    throw new IllegalArgumentException();
                }
                
                // Removing to the end of replaced
                merged = new TextReplace(
                        text1.getOldLength(),
                        text1.getOldText(),
                        text1.getNewText().initial(text1.getNewLength() - text2.getOldLength()));
            }
            else if (replaceEndIndex < removeEndIndex) {
                
                final long remainingOldTextFromRemoved = removeEndIndex - replaceEndIndex;
                final long indexIntoReplaced = startPos2 - startPos1;
                
                if (!text1.getNewText().substringMatches(
                        indexIntoReplaced,
                        text2.getOldText(),
                        text2.getOldText().length() - remainingOldTextFromRemoved)) {

                    throw new IllegalArgumentException();
                }
                
                merged = new TextReplace(
                        text1.getOldLength() + remainingOldTextFromRemoved,
                        text1.getOldText().merge(text2.getOldText().trailing(remainingOldTextFromRemoved)),
                        text1.getNewText().initial(indexIntoReplaced));
            }
            else if (removeEndIndex < replaceEndIndex) {
                
                // Removing in the middle of replaced, e.g. "45", startpos 2 from "3456", startpos 1
                
                // e.g. 2 - 1 = 1 (so at 45 in 3456)
                final long initialLengthFromText1 = startPos2 - startPos1; 
                
                // e.g "3456".length() - "45".length() - 1 = 1
                final long trailingFromText1 = text1.getNewLength() - text2.getOldLength() - initialLengthFromText1;

                if (!text1.getNewText().substringMatches(initialLengthFromText1, text2.getOldText())) {
                    throw new IllegalArgumentException();
                }

                merged = new TextReplace(
                        text1.getOldLength(),
                        text1.getOldText(),
                        text1.getNewText().initial(initialLengthFromText1)
                            .merge(text1.getNewText().trailing(trailingFromText1)));
            }
            else {
                throw new IllegalStateException();
            }
        }

        return merged;
    }

    private static TextEdit mergeRemoveWithAdd(TextRemove text1, long startPos1, TextAdd text2, long startPos2) {

        final TextEdit merged;

        if (startPos1 < startPos2) {
            // Should only call merge() when text1 is before or at text2
            throw new IllegalArgumentException();
        }
        else if (startPos1 == startPos2) {
            
            // remove + add is a replace
            merged = new TextReplace(
                    text1.getOldLength(),
                    text1.getOldText(),
                    text2.getNewText());
        }
        else {
            // startPos2 > startPos does not make sense for remove + add, not contiguous and cannot be merged
            throw new IllegalArgumentException();
        }

        return merged;
    }

    private static TextEdit mergeRemoveWithReplace(TextRemove text1, long startPos1, TextReplace text2, long startPos2) {
        
        final TextEdit merged;
        
        if (startPos2 < startPos1) {

            // Should only call merge() when text1 is before or at text2
            throw new IllegalArgumentException();
        }
        else if (startPos1 == startPos2) {

            merged = new TextReplace(
                    text1.getOldLength() + text2.getOldLength(),
                    text1.getOldText().merge(text2.getOldText()),
                    text2.getNewText());
        }
        else {

            // startPos2 > startPos does not make sense for remove + remove, not contiguous and cannot be merged
            throw new IllegalArgumentException();
        }

        return merged;
    }

    private static TextEdit mergeRemoveWithRemove(TextRemove text1, long startPos1, TextRemove text2, long startPos2) {

        final TextEdit merged;
        
        if (startPos2 < startPos1) {
            // Should only call merge() when text1 is before or at text2
            throw new IllegalArgumentException();
        }
        else if (startPos1 == startPos2) {
            
            // Merge removes, text1 before text2 since text2 was originally after text1
            merged = new TextRemove(
                    text1.getOldLength() + text2.getOldLength(),
                    text1.getOldText().merge(text2.getOldText()));
        }
        else {
            // startPos2 > startPos does not make sense for remove + remove, not contiguous and cannot be merged
            throw new IllegalArgumentException();
        }
        
        return merged;
    }

}
