package dev.nimbler.ide.component.console.output.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import dev.nimbler.ide.component.common.output.UIProcessOutputComponent.ProcessSource;
import dev.nimbler.ide.model.text.TextModel;
import dev.nimbler.ide.util.ui.text.LineDelimiter;
import dev.nimbler.ide.util.ui.text.StringText;
import dev.nimbler.ide.util.ui.text.Text;
import dev.nimbler.ide.util.ui.text.TextRange;
import dev.nimbler.ide.util.ui.text.styling.TextColor;
import dev.nimbler.ide.util.ui.text.styling.TextStyleOffset;
import dev.nimbler.ide.util.ui.text.styling.TextStylingModel;

final class ConsoleTextModel extends TextModel implements TextStylingModel {
    
    private static final Boolean DEBUG = false;

    private final ConsoleTexts texts;

    private long totalLineCount;
    
    // Offset to initial text in text view as counter
    // from start of console output or clear of console 
    private long startOffsetOfTextView;
    
    private long maxDisplayed;
    
    ConsoleTextModel(LineDelimiter lineDelimiter, long maxDisplayed) {
        this(lineDelimiter, maxDisplayed, 0L);
    }

    ConsoleTextModel(LineDelimiter lineDelimiter, long maxDisplayed, long startOffsetOfTextView) {
        super(lineDelimiter);
        
        if (maxDisplayed <= 0) {
            throw new IllegalArgumentException();
        }

        this.maxDisplayed = maxDisplayed;
        this.startOffsetOfTextView = startOffsetOfTextView;

        this.texts = new ListConsoleTexts();
    }

    void add(ProcessSource source, String string) {
        
        final LineDelimiter lineDelimiter = getLineDelimiter();
        
        if (string.isEmpty()) {
            throw new IllegalArgumentException();
        }
        
        if (texts.isEmpty()) {
            final StringText text = new StringText(string);

            totalLineCount = text.getNumberOfLinesWithoutTrailingNonTerminated(lineDelimiter);

            if (DEBUG) {
                System.out.println("## number of lines " + totalLineCount + " for '" + string + "'");
            }

            texts.add(new ConsoleText(source, text, 0L, totalLineCount, totalLineCount));
        }
        else if (texts.getLast().hasSplitNewline()) {

            final ConsoleText last = texts.getLast();
            
            final long prevLineCount = texts.size() > 1 ? texts.getLast(1).totalLineCount : 0L;

            final ConsoleText merged = last.merge(
                    source,
                    string,
                    prevLineCount,
                    lineDelimiter);
            
            texts.replaceLast(merged);
            
            totalLineCount = prevLineCount + merged.text.getNumberOfLinesWithoutTrailingNonTerminated(lineDelimiter);
        }
        else {
            // Subtract last and then add sum of last and this

            final StringText text = new StringText(string);
            
            final ConsoleText last = texts.getLast();
            
            final long textOffset = last.getOffsetFromStart() + getCharCount(last.text);
            
            final long lineCount = text.getNumberOfLinesWithoutTrailingNonTerminated(lineDelimiter);
            
            totalLineCount += lineCount;

            texts.add(new ConsoleText(source, text, textOffset, lineCount, totalLineCount));
        }
        
        final long updatedCharCount = getCharCount();
        
        if (updatedCharCount > maxDisplayed) {
            
            long toRemove = updatedCharCount - maxDisplayed;
            
            int elementsToRemove = 0;
            
            while (toRemove > 0) {
                
                final long firstCharCount = texts.getFirst().getCharCount();
                
                if (firstCharCount < toRemove) {
                    ++ elementsToRemove;
                    
                    toRemove -= firstCharCount;
                }
            }
            
            texts.removeFirst(elementsToRemove);
        }
    }

    @Override
    public Text getText() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void replaceTextRange(long start, long replaceLength, Text text) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Text getTextRange(long start, long length) {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getOffsetAtLine(long lineIndex) {
        
        final long length = getCompleteLength() - startOffsetOfTextView;

        final List<ConsoleText> list = texts.getTextsInRange(startOffsetOfTextView, length);

        if (DEBUG) {
            System.out.println("## getOffsetAtLine " + lineIndex + " from " + list);
        }
        
        final long offset;
        
        if (list.isEmpty()) {
            if (lineIndex != 0L) {
                throw new IllegalArgumentException();
            }
            
            offset = 0L;
        }
        else {
            final LineDelimiter lineDelimiter = getLineDelimiter();

            ConsoleText last = null;
            
            long counter = 0L;
            
            boolean foundLine = false;
            
            for (ConsoleText consoleText : list) {

                if (DEBUG) {
                    System.out.println("## compare " + consoleText.totalLineCount + " to " + lineIndex);
                }

                if (   consoleText.totalLineCount > lineIndex
                        ||  (    consoleText.totalLineCount == lineIndex
                        && !consoleText.endsInNewline(lineDelimiter))) {
                    
                    final long index = last != null
                            ? lineIndex - last.totalLineCount
                            : lineIndex;
                 
                    if (DEBUG) {
                        System.out.println("## find at index " + index + " from " + consoleText);
                    }
                    
                    final long pos = consoleText.text.findPosOfLineIndex(index, lineDelimiter);

                    if (pos >= 0) {
                        counter += pos;
                        foundLine = true;
                        break;
                    }
                }
                else {
                    counter += consoleText.text.length();
                }
                
                last = consoleText;
            }
            
            if (!foundLine) {
                throw new IllegalArgumentException();
            }
            
            offset = counter;
        }

        return offset;
    }

    @Override
    public long getLineCount() {
        return texts.isEmpty() ? 1 : totalLineCount;
    }
    
    private static boolean hasAnySplitNewline(List<ConsoleText> consoleTexts) {
        
        boolean hasSplitNewline = false;
        // Merge any that ends in '\r'
        for (ConsoleText consoleText : consoleTexts) {
            if (consoleText.hasSplitNewline()) {
                // Merge necessary
                hasSplitNewline = true;
                break;
            }
        }
        
        return hasSplitNewline;
    }
    
    @SuppressWarnings("unused")
    private static List<ConsoleText> mergeIfSplitNewline(List<ConsoleText> consoleTexts, LineDelimiter lineDelimiter) {
        
        final List<ConsoleText> list;
        
        final boolean mergeNecessary = hasAnySplitNewline(consoleTexts);

        if (mergeNecessary) {
            list = new ArrayList<>(consoleTexts.size());

            final Iterator<ConsoleText> iter = consoleTexts.iterator();

            boolean mergeWithNext = false; 

            while (iter.hasNext()) {
                
                final ConsoleText consoleText = iter.next();
                
                final boolean mergeWithLast = mergeWithNext;

                mergeWithNext = consoleText.text.charAt(consoleText.text.length() - 1) == '\r';
                
                if (mergeWithLast) {
                    
                    final int index = list.size() - 1;
                    
                    final ConsoleText last = list.get(index);

                    final ConsoleText merged = last.merge(
                            consoleText,
                            list.size() > 1 ? list.get(index - 1).totalLineCount : 0L,
                                    lineDelimiter);
                    
                    list.set(index, merged);
                }
                else {
                    list.add(consoleText);
                }
            }
        }
        else {
            list = consoleTexts;
        }
        
        return list;
   }

    @Override
    public long getLineAtOffset(long offset) {
        
        if (offset < 0L) {
            throw new IllegalArgumentException();
        }

        final long offsetFromConsoleText = offset + startOffsetOfTextView;
        
        final long length = getCompleteLength() - startOffsetOfTextView;
        
        final List<ConsoleText> list = texts.getTextsInRange(startOffsetOfTextView, length);

        int lineNo;
        
        if (list.isEmpty()) {
            if (offset > 0L) {
                throw new IllegalArgumentException();
            }
            
            lineNo = 0;
        }
        else if (offset >= length) {
            throw new IllegalArgumentException();
        }
        else {

            final LineDelimiter lineDelimiter = getLineDelimiter();
            
            // final List<ConsoleText> list = mergeIfSplitNewline(consoleTexts, lineDelimiter);
            
            if (hasAnySplitNewline(list)) {
                throw new IllegalStateException();
            }
     
            lineNo = 0;
    
            for (ConsoleText consoleText : list) {
                
                final long charCount = consoleText.getCharCount();
                
                if (offsetFromConsoleText < consoleText.getOffsetFromStart()) {
                    throw new IllegalStateException();
                }
                else if (offsetFromConsoleText < consoleText.getOffsetFromStart() + charCount) {
                    
                    final long index = offsetFromConsoleText - consoleText.getOffsetFromStart();
                    
                    // First element, get from middle
                    final long lineIndex = consoleText.text.findLineIndexAtPos(
                            index,
                            lineDelimiter);
                    
                    lineNo += lineIndex;
                    break;
                }
                else if (offsetFromConsoleText >= consoleText.getOffsetFromStart() + charCount) {
                    
                    // Pqst ConsoleText
                    lineNo += consoleText.text.getNumberOfLinesWithoutTrailingNonTerminated(lineDelimiter);
                }
                else {
                    throw new IllegalStateException();
                }
            }
        }

        return lineNo;
    }

    @Override
    public Text getLineWithoutAnyNewline(long lineIndex) {

        final long length = getCompleteLength() - startOffsetOfTextView;

        if (DEBUG) {
            System.out.println("## get console texts from " + startOffsetOfTextView + " to " + length);
        }
        
        final List<ConsoleText> list = texts.getTextsInRange(startOffsetOfTextView, length);

        if (DEBUG) {
            System.out.println("## console texts " + list);
        }

        Text line;

        if (list.isEmpty()) {
            line = Text.EMPTY_TEXT;
        }
        else {
            ConsoleText last = null;
            
            final LineDelimiter lineDelimiter = getLineDelimiter();
            
            line = null;
            
            final Iterator<ConsoleText> iter = list.iterator();
    
            while (iter.hasNext()) {
    
                final ConsoleText consoleText = iter.next();
    
                if (DEBUG) {
                    System.out.println("## totalLineCount "
                        + consoleText.totalLineCount
                        + "'" + consoleText.text.asString() + "'");
                }
                
                if (    consoleText.totalLineCount > lineIndex
                     ||  (    consoleText.totalLineCount == lineIndex
                           && !consoleText.endsInNewline(lineDelimiter))) {
                    
                    final long lineIndexInConsoleText;
                    
                    if (last != null) {
                        lineIndexInConsoleText = lineIndex - last.totalLineCount;
                    }
                    else {
                        lineIndexInConsoleText = lineIndex;
                    }
                    
                    line = getFromMultipleConsoleTexts(
                            consoleText,
                            lineIndexInConsoleText,
                            iter,
                            lineDelimiter);
                    break;
                }
                
                last = consoleText;
            }
        }
        
        return line;
    }
    
    /**
     * Get line from possibly multiple ConsoleText
     * 
     * @param consoleText text to start scanning at
     * @param lineIndex line index into consoleText
     * @param iterfor iterating following entries if line spans multiple ConsoleText
     * @param lineDelimiter text line delimiter
     * 
     * @return retrieved line
     */
    
    private static Text getFromMultipleConsoleTexts(
            ConsoleText consoleText,
            long lineIndex,
            Iterator<ConsoleText> iter,
            LineDelimiter lineDelimiter) {

        if (DEBUG) {
            System.out.println("## get pos of " + lineIndex + " from " + consoleText);
        }

        final long pos = consoleText.text.findPosOfLineIndex(lineIndex, lineDelimiter);
        
        Text line;
        
        long lineCount = consoleText.lineCountWithoutTrailing;
        
        if (lineCount < lineIndex) {
            throw new IllegalArgumentException();
        }
        else if (lineCount == lineIndex) {
            // Newline may be in next block
            if (DEBUG) {
                System.out.println("## pos " + pos);
            }

            line = consoleText.text.substring(pos, consoleText.text.length());
            
            while (iter.hasNext()) {
                
                final ConsoleText next = iter.next();
                
                lineCount += next.lineCountWithoutTrailing;
                
                if (lineCount == lineIndex) {
                    line = line.merge(next.text);
                }
                else {
                    final Text restOfLine
                        = next.text.getLineWithoutAnyNewline(
                            0L,
                            lineDelimiter);
                 
                    line = line.merge(restOfLine);
                    break;
                }
            }
        }
        else {
            line = consoleText.text.getLineWithoutAnyNewline(pos, lineDelimiter);
        }

        return line;
    }

    @Override
    public Text getLineIncludingAnyNewline(long lineIndex) {
        throw new UnsupportedOperationException();
    }
    
    private static long getCharCount(Text text) {

        return ConsoleText.getCharCount(text);
    }

    @Override
    public long getCharCount() {

        final long charCount;

        if (texts.isEmpty()) {
            charCount = 0L;
        }
        else {
            final ConsoleText last = texts.getLast();
            
            charCount = last.getOffsetFromStart() - startOffsetOfTextView
                    + last.getCharCount();
        }
        
        return charCount;
    }

    @Override
    public long getLength() {
        return getCharCount();
    }
    
    private long getCompleteLength() {
        
        return texts.isEmpty() ? 0L : texts.getLast().getOffsetFromStart() + texts.getLast().getCharCount();
    }

    @Override
    public long find(Text searchText, long startPos, TextRange range, boolean forward, boolean caseSensitive,
            boolean wrapSearch, boolean wholeWord) {
        
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<TextStyleOffset> getLineStyleOffsets(
            long startPosRelativeToEditorStart,
            long length,
            Text lineText) {

        final List<ConsoleText> consoleTexts = texts.getTextsInRange(startPosRelativeToEditorStart, length);
        
        final List<TextStyleOffset> offsets = new ArrayList<>(consoleTexts.size());

        final long startPos = startPosRelativeToEditorStart + startOffsetOfTextView;

        long leftToProcess = length;
        
        final TextColor stderrColor = new TextColor(0xA0, 0xA0, 0xA0);
        
        for (ConsoleText consoleText : consoleTexts) {
            
            if (consoleText.getSource() != ProcessSource.STDERR) {
                continue;
            }
            
            final long charCount = consoleText.getCharCount();
            
            final long toProcess;
            
            if (startPos < consoleText.getOffsetFromStart()) {
                throw new IllegalStateException();
            }
            else if (startPos == consoleText.getOffsetFromStart()) {
                
                // First element, get from middle

                toProcess = Math.min(leftToProcess, charCount);
            }
            else if (startPos > consoleText.getOffsetFromStart()) {
                
                if (startPos > consoleText.getOffsetFromStart() + charCount) {
                    throw new IllegalStateException();
                }
                
                final long startPosInText = startPos - consoleText.getOffsetFromStart();
                
                toProcess = Math.min(leftToProcess, charCount - startPosInText);
            }
            else {
                throw new IllegalStateException();
            }

            offsets.add(new TextStyleOffset(startPos, toProcess, stderrColor));
            
            leftToProcess -= toProcess;
        }

        return offsets;
    }
}
