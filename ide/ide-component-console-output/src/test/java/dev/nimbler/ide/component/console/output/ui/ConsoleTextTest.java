package dev.nimbler.ide.component.console.output.ui;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import dev.nimbler.ide.component.common.output.UIProcessOutputComponent.ProcessSource;
import dev.nimbler.ide.util.ui.text.StringText;
import dev.nimbler.ide.util.ui.text.WindowsLineDelimiter;

public class ConsoleTextTest {

    @Test
    public void testGetCharCount() {

        final ConsoleText consoleText = makeConsoleText("123");
        
        assertThat(consoleText.getCharCount())
            .isEqualTo(3);
        
    }

    @Test
    public void testGetCharCountWithNewLine() {

        final ConsoleText consoleText = makeConsoleText("123\r\n");
        
        assertThat(consoleText.getCharCount())
            .isEqualTo(5);
        
    }

    @Test
    public void testGetCharCountWithSplitNewLine() {

        final ConsoleText consoleText = makeConsoleText("123\r");
        
        assertThat(consoleText.getCharCount())
            .isEqualTo(4);
        
    }

    static ConsoleText makeConsoleText(String text) {
        
        final StringText stringText = new StringText(text);
        
        final long numberOfLines = stringText.getNumberOfLinesWithoutTrailingNonTerminated(WindowsLineDelimiter.INSTANCE);
        
        return makeConsoleText(stringText, 0L, numberOfLines, numberOfLines);
    }

    static ConsoleText makeConsoleText(String text, long startOffset, long totalNumberOfLines) {

        final StringText stringText = new StringText(text);

        final long numberOfLines = stringText.getNumberOfLinesWithoutTrailingNonTerminated(WindowsLineDelimiter.INSTANCE);
        
        return makeConsoleText(stringText, startOffset, numberOfLines, totalNumberOfLines);
    }
    
    static ConsoleText makeConsoleText(StringText text, long startOffset, long numberOfLines, long totalNumberOfLines) {
        
        final ConsoleText consoleText
            = new ConsoleText(
                    ProcessSource.STDOUT,
                    text,
                    startOffset,
                    numberOfLines,
                    totalNumberOfLines);
        
        return consoleText;
    }
}
