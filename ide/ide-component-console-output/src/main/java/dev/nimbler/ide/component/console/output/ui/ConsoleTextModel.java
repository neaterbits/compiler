package dev.nimbler.ide.component.console.output.ui;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import dev.nimbler.ide.component.common.output.UIProcessOutputComponent.ProcessSource;
import dev.nimbler.ide.component.console.output.ui.ConsoleTexts.ConsoleTextMatcher;
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

    // Line count from initial text in text view as counter
    // from start of console output or clear of console 
    private long totalLineCount;
    
    // Offset to initial text in text view as counter
    // from start of console output or clear of console 
    private long startOffsetOfTextView;
    
    // Line offset to initial text in text view as counter
    // from start of console output or clear of console 
    private long startLineOfTextView;
    
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
                
                if (firstCharCount <= toRemove) {
                    ++ elementsToRemove;
                    
                    toRemove -= firstCharCount;
                }
                else {
                	break;
                }

                final List<ConsoleText> removedTexts = texts.removeFirst(elementsToRemove);

                for (ConsoleText removed : removedTexts) {
                	startLineOfTextView += removed.lineCountWithoutTrailing;
                	startOffsetOfTextView += removed.getCharCount();
                }
            }
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
        
        if (lineIndex < 0) {
            throw new IllegalArgumentException();
        }
        
        if (lineIndex > totalLineCount) {
            throw new IllegalArgumentException();
        }

        if (DEBUG) {
            System.out.println("## getOffsetAtLine " + lineIndex + " from " + texts);
        }
        
        final long offset;
        
        if (texts.isEmpty()) {
            if (lineIndex != 0L) {
                throw new IllegalArgumentException();
            }
            
            offset = 0L;
        }
        else {
            final LineDelimiter lineDelimiter = getLineDelimiter();

            if (lineIndex == totalLineCount && texts.getLast().endsInNewline(lineDelimiter)) {
                throw new IllegalArgumentException();
            }
            
            final List<ConsoleText> nodes = getNodesContainingLine(lineIndex, lineDelimiter);

            if (nodes.isEmpty()) {
                throw new IllegalStateException();
            }
            
            final ConsoleText initial = nodes.get(0);
            
            final long lineIndexFromStart = getLineIndexFromStart(lineIndex);
            
            final long lineIndexInText = lineIndexFromStart - initial.getIndexOfInitialLine();
            
            final long offsetInText = initial.text.findPosOfLineIndex(lineIndexInText, lineDelimiter);
            
            offset = (initial.getOffsetFromStart() - startOffsetOfTextView) + offsetInText;
        }

        return offset;
    }
    
    private long getLineIndexFromStart(long lineIndexFromVisible) {
        
        final long lineIndexFromStart = lineIndexFromVisible + startLineOfTextView;

        return lineIndexFromStart;
    }
    
    private List<ConsoleText> getNodesContainingLine(long lineIndexFromVisible, LineDelimiter lineDelimiter) {
    	
    	final long lineIndexFromStart = getLineIndexFromStart(lineIndexFromVisible);
    	
    	final ConsoleTextMatcher matcher = new ConsoleTextMatcher() {
			
			@Override
			public boolean isMatching(ConsoleText consoleText) {
				
				final boolean totalLineCountIsAfterLineIndex
					=            consoleText.totalLineCount > lineIndexFromStart
		                ||  (    consoleText.totalLineCount == lineIndexFromStart
		                  && !consoleText.endsInNewline(lineDelimiter));

					return 
						   lineIndexFromStart >= consoleText.getIndexOfInitialLine()
						&& totalLineCountIsAfterLineIndex;
			}
		};

		return texts.getTextsMatching(matcher);
    }

    @Override
    public long getLineCount() {
        return texts.isEmpty() ? 1 : totalLineCount - startLineOfTextView;
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
        
        final long lineIndexFromStart = getLineIndexFromStart(lineIndex);

        final LineDelimiter lineDelimiter = getLineDelimiter();
        
        final long length = getCompleteLength() - startOffsetOfTextView;

        if (DEBUG) {
            System.out.println("## get console texts from " + startOffsetOfTextView + " to " + length);
        }
        
        final List<ConsoleText> list = getNodesContainingLine(lineIndex, lineDelimiter);

        if (DEBUG) {
            System.out.println("## console texts " + list);
        }

        Text line;

        if (list.isEmpty()) {
            line = Text.EMPTY_TEXT;
        }
        else if (list.size() == 1) {
            final ConsoleText consoleText = list.get(0);
         
            line = getLineFromConsoleText(consoleText, lineIndexFromStart, lineDelimiter);
        }
        else {
            final StringBuilder sb = new StringBuilder();
            
            // Initial line
            final ConsoleText initialConsoleText = list.get(0);
            
            final long lineIndexInInitial = getLineIndexInText(initialConsoleText, lineIndexFromStart);
            final long posInInitial = initialConsoleText.text.findPosOfLineIndex(lineIndexInInitial, lineDelimiter);
            
            final Text initial = initialConsoleText.text.substring(posInInitial);
            
            sb.append(initial.asString());
            
            for (int i = 1; i < list.size() - 1; ++ i) {
                sb.append(list.get(i).text.asString());
            }
            
            final ConsoleText lastConsoleText = list.get(list.size() - 1);

            final Text last = getLineFromConsoleTextIndex(lastConsoleText, 0L, lineDelimiter);
            
            sb.append(last.asString());
            
            line = new StringText(sb.toString());
        }

        return line;
    }
    
    private long getLineIndexInText(ConsoleText consoleText, long lineIndexFromStart) {
     
        return lineIndexFromStart - consoleText.getIndexOfInitialLine();
    }
    
    private Text getLineFromConsoleText(ConsoleText consoleText, long lineIndexFromStart, LineDelimiter lineDelimiter) {

        final long lineIndexInText = getLineIndexInText(consoleText, lineIndexFromStart);

        return getLineFromConsoleTextIndex(consoleText, lineIndexInText, lineDelimiter);
    }

    private Text getLineFromConsoleTextIndex(ConsoleText consoleText, long lineIndexInText, LineDelimiter lineDelimiter) {

        final long pos = consoleText.text.findPosOfLineIndex(lineIndexInText, lineDelimiter);
        
        return consoleText.text.getLineWithoutAnyNewlineOrTillEndOfString(pos, lineDelimiter);
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

	@Override
	public String toString() {
		return "ConsoleTextModel [texts=" + texts + ", totalLineCount=" + totalLineCount + ", startOffsetOfTextView="
				+ startOffsetOfTextView + ", maxDisplayed=" + maxDisplayed + "]";
	}
}
