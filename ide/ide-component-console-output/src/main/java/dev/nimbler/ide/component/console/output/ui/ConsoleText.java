package dev.nimbler.ide.component.console.output.ui;

import dev.nimbler.ide.component.common.output.UIProcessOutputComponent.ProcessSource;
import dev.nimbler.ide.util.ui.text.LineDelimiter;
import dev.nimbler.ide.util.ui.text.Text;

final class ConsoleText {

    private final ProcessSource source;
    final Text text;
    
    // offset from start of output start or last console clear
    private final long offsetFromStart;

    // Number of terminated lines in block
    final long lineCountWithoutTrailing;
    
    // line offset from start of output start or last console clear
    final long totalLineCount;
    
    static long getCharCount(Text text) {
        
        return text.length();
    }
    
    ConsoleText(ProcessSource source, Text text, long offsetFromStart, long totalLineCount, LineDelimiter lineDelimiter) {
        this(source, text, offsetFromStart, text.getNumberOfLinesWithoutTrailingNonTerminated(lineDelimiter), totalLineCount);
    }

    ConsoleText(ProcessSource source, Text text, long offsetFromStart, long lineCount, long totalLineCount) {
        this.source = source;
        this.text = text;
        this.offsetFromStart = offsetFromStart;
        this.lineCountWithoutTrailing = lineCount;
        this.totalLineCount = totalLineCount;
    }
    
    ConsoleText merge(ConsoleText other, long prevTotalLineCount, LineDelimiter lineDelimiter) {
        
        if (source != other.source) {
            throw new IllegalArgumentException();
        }
        
        final Text merged = text.merge(other.text);
        
        final long lineCount = merged.getNumberOfLinesWithoutTrailingNonTerminated(lineDelimiter);
        
        return new ConsoleText(
                source,
                merged,
                offsetFromStart,
                lineCount,
                prevTotalLineCount + lineCount);
    }

    ConsoleText merge(ProcessSource source, String other, long prevTotalLineCount, LineDelimiter lineDelimiter) {

        if (this.source != source) {
            throw new IllegalArgumentException();
        }

        final Text merged = text.merge(other);
        
        final long lineCount = merged.getNumberOfLinesWithoutTrailingNonTerminated(lineDelimiter);
        
        return new ConsoleText(
                this.source,
                merged,
                offsetFromStart,
                lineCount,
                prevTotalLineCount + lineCount);
    }

    ProcessSource getSource() {
        return source;
    }

    long getOffsetFromStart() {
        return offsetFromStart;
    }
    
    long getCharCount() {
        return getCharCount(text);
    }
    
    boolean hasSplitNewline() {
        return text.charAt(text.length() - 1) == '\r';
    }
    
    boolean endsInNewline(LineDelimiter lineDelimiter) {
        return lineDelimiter.endsWithNewline(text);
    }
    
    long getIndexOfInitialLine() {
        return totalLineCount - lineCountWithoutTrailing;        
    }

    @Override
    public String toString() {
        return "ConsoleText [source=" + source + ", text=" + text + ", offsetFromStart=" + offsetFromStart
                + ", lineCount=" + lineCountWithoutTrailing + ", totalLineCount=" + totalLineCount + "]";
    }
}
