package dev.nimbler.ide.component.console.output.ui;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import java.util.function.Function;

import org.junit.Test;

import dev.nimbler.ide.component.common.output.UIProcessOutputComponent.ProcessSource;
import dev.nimbler.ide.util.ui.text.LineDelimiter;
import dev.nimbler.ide.util.ui.text.UnixLineDelimiter;
import dev.nimbler.ide.util.ui.text.WindowsLineDelimiter;

public class ConsoleTextModelTest {

    @Test
    public void testEmpty() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                                            WindowsLineDelimiter.INSTANCE,
                                            Long.MAX_VALUE);

        assertThat(model.getCharCount()).isEqualTo(0L);
        assertThat(model.getLength()).isEqualTo(0L);
        
        assertThat(model.getLineCount()).isEqualTo(1L);
        
        assertThat(model.getLineDelimiter()).isSameAs(WindowsLineDelimiter.INSTANCE);
        // assertThat(model.getTextRange(0L, 0L).asString()).isEqualTo("");
    }
    
    @Test
    public void testGetLineAtOffset() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "123\r\n");
        model.add(ProcessSource.STDOUT, "456\r\n");
        
        assertThat(model.getCharCount()).isEqualTo(10);
      
        checkGetLineAtOffsetIllegalArgument(model, -1);

        assertThat(model.getLineAtOffset(0L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(1L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(2L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(3L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(4L)).isEqualTo(0L);

        assertThat(model.getLineAtOffset(5L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(6L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(7L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(8L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(9L)).isEqualTo(1L);

        checkGetLineAtOffsetIllegalArgument(model, 10);

        assertThat(model.getLineCount()).isEqualTo(2);
    }

    @Test
    public void testGetLineAtOffsetWithMiddleOfLineShift() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "123\r");
        model.add(ProcessSource.STDOUT, "\n456\r\n");
        
        assertThat(model.getCharCount()).isEqualTo(10);
      
        checkGetLineAtOffsetIllegalArgument(model, -1);

        assertThat(model.getLineAtOffset(0L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(1L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(2L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(3L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(4L)).isEqualTo(0L);

        assertThat(model.getLineAtOffset(5L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(6L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(7L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(8L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(9L)).isEqualTo(1L);

        checkGetLineAtOffsetIllegalArgument(model, 10);

        assertThat(model.getLineCount()).isEqualTo(2);
    }

    @Test
    public void testGetLineAtOffsetWithStartOffset() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE,
                6L);
        
        model.add(ProcessSource.STDOUT, "1234\r\n");
        model.add(ProcessSource.STDOUT, "56\r\n");
        model.add(ProcessSource.STDOUT, "789\r\n");
        
        assertThat(model.getCharCount()).isEqualTo(9);

        checkGetLineAtOffsetIllegalArgument(model, -1);
      
        assertThat(model.getLineAtOffset(0L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(1L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(2L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(3L)).isEqualTo(0L);
        
        assertThat(model.getLineAtOffset(4L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(5L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(6L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(7L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(8L)).isEqualTo(1L);

        checkGetLineAtOffsetIllegalArgument(model, 9);

        assertThat(model.getLineCount()).isEqualTo(3);
    }

    @Test
    public void testGetLineAtOffsetWithStartOffsetInMiddleOfLine() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE,
                7L);
        
        model.add(ProcessSource.STDOUT, "1234\r\n");
        model.add(ProcessSource.STDOUT, "56\r\n");
        model.add(ProcessSource.STDOUT, "789\r\n");
        
        assertThat(model.getCharCount()).isEqualTo(8);

        checkGetLineAtOffsetIllegalArgument(model, -1);
      
        assertThat(model.getLineAtOffset(0L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(1L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(2L)).isEqualTo(0L);

        assertThat(model.getLineAtOffset(3L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(4L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(5L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(6L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(7L)).isEqualTo(1L);

        checkGetLineAtOffsetIllegalArgument(model, 8);

        assertThat(model.getLineCount()).isEqualTo(3);
    }

    @Test
    public void testGetLineAtOffsetWithStartOffsetAndOffsetMiddleOfLineShift() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE,
                6L);
        
        model.add(ProcessSource.STDOUT, "1234\r\n");
        model.add(ProcessSource.STDOUT, "56\r\n");
        model.add(ProcessSource.STDOUT, "789\r\n");
        
        assertThat(model.getCharCount()).isEqualTo(9);

        checkGetLineAtOffsetIllegalArgument(model, -1);
      
        assertThat(model.getLineAtOffset(0L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(1L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(2L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(3L)).isEqualTo(0L);
        
        assertThat(model.getLineAtOffset(4L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(5L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(6L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(7L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(8L)).isEqualTo(1L);

        checkGetLineAtOffsetIllegalArgument(model, 9);

        assertThat(model.getLineCount()).isEqualTo(3);
    }

    @Test
    public void testGetLineAtOffsetEmpty() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        checkGetLineAtOffsetIllegalArgument(model, -1);

        assertThat(model.getLineAtOffset(0L)).isEqualTo(0L);
    
        checkGetLineAtOffsetIllegalArgument(model, 1);
    }

    @Test
    public void testGetLineAtOffsetWithoutNewlineEmptyString() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "\r\n");

        checkGetLineAtOffsetIllegalArgument(model, -1);

        assertThat(model.getLineAtOffset(0L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(1L)).isEqualTo(0L);

        checkGetLineAtOffsetIllegalArgument(model, 2);
    }

    @Test
    public void testGetLineAtOffsetWithoutNewlineOneLineNoNewline() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "1234");

        checkGetLineAtOffsetIllegalArgument(model, -1);

        assertThat(model.getLineAtOffset(0L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(1L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(2L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(3L)).isEqualTo(0L);

        checkGetLineAtOffsetIllegalArgument(model, 4);
    }

    @Test
    public void testGetLineAtOffsetWithoutNewlineOneLinEndingInNewline() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "1234\r\n");

        checkGetLineAtOffsetIllegalArgument(model, -1);

        assertThat(model.getLineAtOffset(0L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(1L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(2L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(3L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(4L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(5L)).isEqualTo(0L);

        checkGetLineAtOffsetIllegalArgument(model, 6);
    }

    @Test
    public void testGetLineAtOffsetAcrossTwoTexts() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "12\r\n34");
        model.add(ProcessSource.STDOUT, "5\r\n6");
        model.add(ProcessSource.STDOUT, "789\r\n");

        checkGetLineAtOffsetIllegalArgument(model, -1);

        assertThat(model.getLineAtOffset(0L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(1L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(2L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(3L)).isEqualTo(0L);

        assertThat(model.getLineAtOffset(4L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(5L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(6L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(7L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(8L)).isEqualTo(1L);
        
        assertThat(model.getLineAtOffset(9L)).isEqualTo(2L);
        assertThat(model.getLineAtOffset(10L)).isEqualTo(2L);
        assertThat(model.getLineAtOffset(11L)).isEqualTo(2L);
        assertThat(model.getLineAtOffset(12L)).isEqualTo(2L);
        assertThat(model.getLineAtOffset(13L)).isEqualTo(2L);
        assertThat(model.getLineAtOffset(14L)).isEqualTo(2L);

        checkGetLineAtOffsetIllegalArgument(model, 15);
    }

    @Test
    public void testGetLineAtOffsetAcrossTwoTextsNotInitial() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        checkGetLineAtOffsetIllegalArgument(model, -1);

        model.add(ProcessSource.STDOUT, "0\r\n");
        model.add(ProcessSource.STDOUT, "12\r\n34");
        model.add(ProcessSource.STDOUT, "5\r\n6");
        model.add(ProcessSource.STDOUT, "789\r\n");

        assertThat(model.getLineAtOffset(0L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(1L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(2L)).isEqualTo(0L);
        
        assertThat(model.getLineAtOffset(3L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(4L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(5L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(6L)).isEqualTo(1L);

        assertThat(model.getLineAtOffset(7L)).isEqualTo(2L);
        assertThat(model.getLineAtOffset(8L)).isEqualTo(2L);
        assertThat(model.getLineAtOffset(9L)).isEqualTo(2L);
        assertThat(model.getLineAtOffset(10L)).isEqualTo(2L);
        assertThat(model.getLineAtOffset(11L)).isEqualTo(2L);
        
        assertThat(model.getLineAtOffset(12L)).isEqualTo(3L);
        assertThat(model.getLineAtOffset(13L)).isEqualTo(3L);
        assertThat(model.getLineAtOffset(14L)).isEqualTo(3L);
        assertThat(model.getLineAtOffset(15L)).isEqualTo(3L);
        assertThat(model.getLineAtOffset(16L)).isEqualTo(3L);
        assertThat(model.getLineAtOffset(17L)).isEqualTo(3L);

        checkGetLineAtOffsetIllegalArgument(model, 18);
    }

    @Test
    public void testGetLineAtOffsetAcrossMultipleTexts() {
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "12\r\n34");
        model.add(ProcessSource.STDOUT, "56");
        model.add(ProcessSource.STDOUT, "78\r\n9\r\n");
        
        checkGetLineAtOffsetIllegalArgument(model, -1);

        assertThat(model.getLineAtOffset(0L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(1L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(2L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(3L)).isEqualTo(0L);
        
        assertThat(model.getLineAtOffset(4L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(5L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(6L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(7L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(8L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(9L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(10L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(11L)).isEqualTo(1L);
        
        assertThat(model.getLineAtOffset(12L)).isEqualTo(2L);
        assertThat(model.getLineAtOffset(13L)).isEqualTo(2L);
        assertThat(model.getLineAtOffset(14L)).isEqualTo(2L);
        
        checkGetLineAtOffsetIllegalArgument(model, 15);
    }
    
    @Test
    public void testGetLineAtOffsetAcrossMultipleTextsStartingAtNewline() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "1234\r\n");
        model.add(ProcessSource.STDOUT, "56");
        model.add(ProcessSource.STDOUT, "789");
        model.add(ProcessSource.STDOUT, "01\r\n2\r\n");

        checkGetLineAtOffsetIllegalArgument(model, -1);

        assertThat(model.getLineAtOffset(0L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(1L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(2L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(3L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(4L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(5L)).isEqualTo(0L);
        
        assertThat(model.getLineAtOffset(6L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(7L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(8L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(9L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(10L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(11L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(12L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(13L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(14L)).isEqualTo(1L);
        
        assertThat(model.getLineAtOffset(15L)).isEqualTo(2L);
        assertThat(model.getLineAtOffset(16L)).isEqualTo(2L);
        assertThat(model.getLineAtOffset(17L)).isEqualTo(2L);

        checkGetLineAtOffsetIllegalArgument(model, 18);
    }

    @Test
    public void testGetLineAtOffsetAcrossMultipleTextsEndingAtNewline() {
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "12\r\n34");
        model.add(ProcessSource.STDOUT, "56");
        model.add(ProcessSource.STDOUT, "789\r\n");
        model.add(ProcessSource.STDOUT, "01\r\n2\r\n");
        
        checkGetLineAtOffsetIllegalArgument(model, -1);

        assertThat(model.getLineAtOffset(0L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(1L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(2L)).isEqualTo(0L);
        assertThat(model.getLineAtOffset(3L)).isEqualTo(0L);
        
        assertThat(model.getLineAtOffset(4L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(5L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(6L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(7L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(8L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(9L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(10L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(11L)).isEqualTo(1L);
        assertThat(model.getLineAtOffset(12L)).isEqualTo(1L);
        
        assertThat(model.getLineAtOffset(13L)).isEqualTo(2L);
        assertThat(model.getLineAtOffset(14L)).isEqualTo(2L);
        assertThat(model.getLineAtOffset(15L)).isEqualTo(2L);
        assertThat(model.getLineAtOffset(16L)).isEqualTo(2L);
        
        assertThat(model.getLineAtOffset(17L)).isEqualTo(3L);
        assertThat(model.getLineAtOffset(18L)).isEqualTo(3L);
        assertThat(model.getLineAtOffset(19L)).isEqualTo(3L);

        checkGetLineAtOffsetIllegalArgument(model, 20);
    }
    
    @Test
    public void testGetLineEmpty() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        assertThat(model.getLineWithoutAnyNewline(0L).asString()).isEqualTo("");
    }

    @Test
    public void testGetLineWithoutNewlineEmptyString() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "\r\n");

        assertThat(model.getLineWithoutAnyNewline(0L).asString()).isEqualTo("");
    }

    @Test
    public void testGetLineWithoutNewlineOneLineNoNewline() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "1234");

        assertThat(model.getLineWithoutAnyNewline(0L).asString()).isEqualTo("1234");
    }

    @Test
    public void testGetLineWithoutNewlineOneLinEndingInNewline() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "1234\r\n");

        assertThat(model.getLineWithoutAnyNewline(0L).asString()).isEqualTo("1234");
    }

    @Test
    public void testGetLineAcrossTwoTexts() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "12\r\n34");
        model.add(ProcessSource.STDOUT, "5\r\n6");
        model.add(ProcessSource.STDOUT, "789\r\n");

        assertThat(model.getLineWithoutAnyNewline(1L).asString()).isEqualTo("345");
    }

    @Test
    public void testGetLineAcrossTwoTextsNotInitial() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "0\r\n");
        model.add(ProcessSource.STDOUT, "12\r\n34");
        model.add(ProcessSource.STDOUT, "5\r\n6");
        model.add(ProcessSource.STDOUT, "789\r\n");

        assertThat(model.getLineWithoutAnyNewline(2L).asString()).isEqualTo("345");
    }

    @Test
    public void testGetLineAcrossMultipleTexts() {
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "12\r\n34");
        model.add(ProcessSource.STDOUT, "56");
        model.add(ProcessSource.STDOUT, "78\r\n9\r\n");
        
        assertThat(model.getLineWithoutAnyNewline(1L).asString()).isEqualTo("345678");
    }
    
    @Test
    public void testGetLineAcrossMultipleTextsStartingAtNewline() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "1234\r\n");
        model.add(ProcessSource.STDOUT, "56");
        model.add(ProcessSource.STDOUT, "789");
        model.add(ProcessSource.STDOUT, "01\r\n2\r\n");

        assertThat(model.getLineWithoutAnyNewline(1L).asString()).isEqualTo("5678901");
    }

    @Test
    public void testGetLineAcrossMultipleTextsEndingAtNewline() {
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "12\r\n34");
        model.add(ProcessSource.STDOUT, "56");
        model.add(ProcessSource.STDOUT, "789\r\n");
        model.add(ProcessSource.STDOUT, "01\r\n2\r\n");
        
        assertThat(model.getLineWithoutAnyNewline(1L).asString()).isEqualTo("3456789");
    }

    @Test
    public void testGetLineWithinLine() {
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "12\r\n");
        model.add(ProcessSource.STDOUT, "56\r\n789\r\n01");
        model.add(ProcessSource.STDOUT, "\r\n2\r\n");
        
        assertThat(model.getLineWithoutAnyNewline(2L).asString()).isEqualTo("789");
    }

    @Test
    public void testGetOffsetAtLineEmpty() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        checkGetOffsetAtLineIllegalArgument(model, -1);

        assertThat(model.getOffsetAtLine(0L)).isEqualTo(0L);

        checkGetOffsetAtLineIllegalArgument(model, 1);
    }

    @Test
    public void testGetOffsetAtLineWithoutNewlineEmptyString() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "\r\n");

        checkGetOffsetAtLineIllegalArgument(model, -1);

        assertThat(model.getOffsetAtLine(0L)).isEqualTo(0L);

        checkGetOffsetAtLineIllegalArgument(model, 1);
    }

    @Test
    public void testGetOffsetAtLineWithoutNewlineOneLineNoNewline() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "1234");

        checkGetOffsetAtLineIllegalArgument(model, -1);

        assertThat(model.getOffsetAtLine(0L)).isEqualTo(0L);

        checkGetOffsetAtLineIllegalArgument(model, 1);
    }

    @Test
    public void testGetOffsetAtLineWithoutNewlineOneLinEndingInNewline() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "1234\r\n");

        checkGetOffsetAtLineIllegalArgument(model, -1);

        assertThat(model.getOffsetAtLine(0L)).isEqualTo(0L);

        checkGetOffsetAtLineIllegalArgument(model, 1);
    }

    @Test
    public void testGetOffsetAtLineAcrossTwoTexts() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "12\r\n34");
        model.add(ProcessSource.STDOUT, "5\r\n6");
        model.add(ProcessSource.STDOUT, "789\r\n");

        checkGetOffsetAtLineIllegalArgument(model, -1);

        assertThat(model.getOffsetAtLine(0L)).isEqualTo(0L);
        assertThat(model.getOffsetAtLine(1L)).isEqualTo(4L);
        assertThat(model.getOffsetAtLine(2L)).isEqualTo(9L);

        checkGetOffsetAtLineIllegalArgument(model, 3);
    }
    
    @Test
    public void testGetOffsetAtLineAcrossTwoTextsNotInitial() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "0\r\n");
        model.add(ProcessSource.STDOUT, "12\r\n34");
        model.add(ProcessSource.STDOUT, "5\r\n6");
        model.add(ProcessSource.STDOUT, "789\r\n");

        checkGetOffsetAtLineIllegalArgument(model, -1);

        assertThat(model.getOffsetAtLine(0L)).isEqualTo(0L);
        assertThat(model.getOffsetAtLine(1L)).isEqualTo(3L);
        assertThat(model.getOffsetAtLine(2L)).isEqualTo(7L);
        assertThat(model.getOffsetAtLine(3L)).isEqualTo(12L);

        checkGetOffsetAtLineIllegalArgument(model, 4);
    }

    @Test
    public void testGetOffsetAtLineAcrossMultipleTexts() {
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "12\r\n34");
        model.add(ProcessSource.STDOUT, "56");
        model.add(ProcessSource.STDOUT, "78\r\n9\r\n");
        
        checkGetOffsetAtLineIllegalArgument(model, -1);

        assertThat(model.getOffsetAtLine(0L)).isEqualTo(0L);
        assertThat(model.getOffsetAtLine(1L)).isEqualTo(4L);
        assertThat(model.getOffsetAtLine(2L)).isEqualTo(12L);

        checkGetOffsetAtLineIllegalArgument(model, 15);
    }
    
    @Test
    public void testGetOffsetAtLineAcrossMultipleTextsStartingAtNewline() {
        
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "1234\r\n");
        model.add(ProcessSource.STDOUT, "56");
        model.add(ProcessSource.STDOUT, "789");
        model.add(ProcessSource.STDOUT, "01\r\n2\r\n");

        checkGetOffsetAtLineIllegalArgument(model, -1);

        assertThat(model.getOffsetAtLine(0L)).isEqualTo(0L);
        assertThat(model.getOffsetAtLine(1L)).isEqualTo(6L);
        assertThat(model.getOffsetAtLine(2L)).isEqualTo(15L);

        checkGetOffsetAtLineIllegalArgument(model, 18);
    }

    @Test
    public void testGetOffsetAtLineAcrossMultipleTextsEndingAtNewline() {
        final ConsoleTextModel model = new ConsoleTextModel(
                WindowsLineDelimiter.INSTANCE,
                Long.MAX_VALUE);
        
        model.add(ProcessSource.STDOUT, "12\r\n34");
        model.add(ProcessSource.STDOUT, "56");
        model.add(ProcessSource.STDOUT, "789\r\n");
        model.add(ProcessSource.STDOUT, "01\r\n2\r\n");
        
        checkGetOffsetAtLineIllegalArgument(model, -1);

        assertThat(model.getOffsetAtLine(0L)).isEqualTo(0L);
        assertThat(model.getOffsetAtLine(1L)).isEqualTo(4L);
        assertThat(model.getOffsetAtLine(2L)).isEqualTo(13L);
        assertThat(model.getOffsetAtLine(3L)).isEqualTo(17L);

        checkGetOffsetAtLineIllegalArgument(model, 20);
    }
    
    @Test
    public void testAddMoreLinesThanMaxVisible() {
    	
    	final LineDelimiter lineDelimiter = UnixLineDelimiter.INSTANCE;
    	
        final ConsoleTextModel model = new ConsoleTextModel(
                lineDelimiter,
                15);
        
        final Function<Integer, String> makeText = i -> "123" + i;
        final Function<Integer, String> makeLine = i -> makeText.apply(i) + lineDelimiter.asString();

    	model.add(ProcessSource.STDOUT, makeLine.apply(0));
    	assertThat(model.getLineCount()).isEqualTo(1L);
    	assertThat(model.getLineWithoutAnyNewline(0).asString()).isEqualTo(makeText.apply(0));
    	
    	model.add(ProcessSource.STDOUT, makeLine.apply(1));
    	assertThat(model.getLineCount()).isEqualTo(2L);
    	assertThat(model.getLineWithoutAnyNewline(0).asString()).isEqualTo(makeText.apply(0));
    	assertThat(model.getLineWithoutAnyNewline(1).asString()).isEqualTo(makeText.apply(1));

    	model.add(ProcessSource.STDOUT, makeLine.apply(2));
    	assertThat(model.getLineCount()).isEqualTo(3L);
    	assertThat(model.getLineWithoutAnyNewline(0).asString()).isEqualTo(makeText.apply(0));
    	assertThat(model.getLineWithoutAnyNewline(1).asString()).isEqualTo(makeText.apply(1));
    	assertThat(model.getLineWithoutAnyNewline(2).asString()).isEqualTo(makeText.apply(2));

    	model.add(ProcessSource.STDOUT, makeLine.apply(3));
    	
    	assertThat(model.getLineCount()).isEqualTo(3L);
    	assertThat(model.getLineWithoutAnyNewline(0).asString()).isEqualTo(makeText.apply(1));
    	assertThat(model.getLineWithoutAnyNewline(1).asString()).isEqualTo(makeText.apply(2));
    	assertThat(model.getLineWithoutAnyNewline(2).asString()).isEqualTo(makeText.apply(3));

    	model.add(ProcessSource.STDOUT, makeLine.apply(4));
    	
    	assertThat(model.getLineCount()).isEqualTo(3L);
    	assertThat(model.getLineWithoutAnyNewline(0).asString()).isEqualTo(makeText.apply(2));
    	assertThat(model.getLineWithoutAnyNewline(1).asString()).isEqualTo(makeText.apply(3));
    	assertThat(model.getLineWithoutAnyNewline(2).asString()).isEqualTo(makeText.apply(4));
    }

    private void checkGetLineAtOffsetIllegalArgument(ConsoleTextModel model, long index) {
        
        try {
            model.getLineAtOffset(index);
            
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }

    private void checkGetOffsetAtLineIllegalArgument(ConsoleTextModel model, long index) {
        
        try {
            model.getOffsetAtLine(index);
            
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }
}
