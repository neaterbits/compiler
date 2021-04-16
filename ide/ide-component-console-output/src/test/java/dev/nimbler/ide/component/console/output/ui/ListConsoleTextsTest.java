package dev.nimbler.ide.component.console.output.ui;

import static dev.nimbler.ide.component.console.output.ui.ConsoleTextTest.makeConsoleText;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class ListConsoleTextsTest {
    
    @Test
    public void testEmpty() {

        final ListConsoleTexts consoleTexts = new ListConsoleTexts();
        
        assertThat(consoleTexts.isEmpty()).isTrue();
        
        assertThat(consoleTexts.getFirst()).isNull();
        assertThat(consoleTexts.getLast()).isNull();
        
        assertThat(consoleTexts.getTextsInRange(0L, 10000L)).isEmpty();
    }

    @Test
    public void testAddOne() {
     
        final ListConsoleTexts consoleTexts = new ListConsoleTexts();
                
        consoleTexts.add(makeConsoleText("123"));

        assertThat(consoleTexts.isEmpty()).isFalse();

        assertThat(consoleTexts.getFirst().text.asString()).isEqualTo("123");
        assertThat(consoleTexts.getLast().text.asString()).isEqualTo("123");

        assertThat(consoleTexts.getTextsInRange(0L, 0L)).isEmpty();

        assertThat(consoleTexts.getTextsInRange(0L, 10000L).size()).isEqualTo(1);
        
        assertThat(consoleTexts.getTextsInRange(0L, 10000L).get(0).text.asString()).isEqualTo("123");
    }

    @Test
    public void testAddMultiple() {

        final ListConsoleTexts consoleTexts = new ListConsoleTexts();

        consoleTexts.add(makeConsoleText("123\r\n", 0L, 1L));
        consoleTexts.add(makeConsoleText("4\r\n56", 5L, 2L));
        consoleTexts.add(makeConsoleText("789\r\n", 10L, 3L));

        assertThat(consoleTexts.isEmpty()).isFalse();

        assertThat(consoleTexts.getFirst().text.asString()).isEqualTo("123\r\n");
        assertThat(consoleTexts.getFirst().getOffsetFromStart()).isEqualTo(0);
        assertThat(consoleTexts.getFirst().getCharCount()).isEqualTo(5);

        assertThat(consoleTexts.getLast().text.asString()).isEqualTo("789\r\n");
        assertThat(consoleTexts.getLast().getOffsetFromStart()).isEqualTo(10);
        assertThat(consoleTexts.getFirst().getCharCount()).isEqualTo(5);

        assertThat(consoleTexts.getTextsInRange(0L, 1L).size()).isEqualTo(1);
        assertThat(consoleTexts.getTextsInRange(0L, 2L).size()).isEqualTo(1);
        assertThat(consoleTexts.getTextsInRange(0L, 3L).size()).isEqualTo(1);
        assertThat(consoleTexts.getTextsInRange(0L, 4L).size()).isEqualTo(1);
        assertThat(consoleTexts.getTextsInRange(0L, 5L).size()).isEqualTo(1);
        
        assertThat(consoleTexts.getTextsInRange(0L, 5L).get(0).text.asString()).isEqualTo("123\r\n");

        assertThat(consoleTexts.getTextsInRange(0L, 6L).size()).isEqualTo(2);
        assertThat(consoleTexts.getTextsInRange(0L, 7L).size()).isEqualTo(2);
        assertThat(consoleTexts.getTextsInRange(0L, 8L).size()).isEqualTo(2);
        assertThat(consoleTexts.getTextsInRange(0L, 9L).size()).isEqualTo(2);
        assertThat(consoleTexts.getTextsInRange(0L, 10L).size()).isEqualTo(2);

        assertThat(consoleTexts.getTextsInRange(0L, 10L).get(0).text.asString()).isEqualTo("123\r\n");
        assertThat(consoleTexts.getTextsInRange(0L, 10L).get(1).text.asString()).isEqualTo("4\r\n56");

        assertThat(consoleTexts.getTextsInRange(0L, 11L).size()).isEqualTo(3);
        assertThat(consoleTexts.getTextsInRange(0L, 12L).size()).isEqualTo(3);
        assertThat(consoleTexts.getTextsInRange(0L, 13L).size()).isEqualTo(3);
        assertThat(consoleTexts.getTextsInRange(0L, 14L).size()).isEqualTo(3);
        assertThat(consoleTexts.getTextsInRange(0L, 15L).size()).isEqualTo(3);
        assertThat(consoleTexts.getTextsInRange(0L, 16L).size()).isEqualTo(3);

        assertThat(consoleTexts.getTextsInRange(0L, 16L).get(0).text.asString()).isEqualTo("123\r\n");
        assertThat(consoleTexts.getTextsInRange(0L, 16L).get(1).text.asString()).isEqualTo("4\r\n56");
        assertThat(consoleTexts.getTextsInRange(0L, 16L).get(2).text.asString()).isEqualTo("789\r\n");

        assertThat(consoleTexts.getTextsInRange(0L, 10000L).size()).isEqualTo(3);
    }
}
