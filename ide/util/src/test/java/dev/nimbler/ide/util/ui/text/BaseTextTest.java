package dev.nimbler.ide.util.ui.text;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

public abstract class BaseTextTest {

    abstract Text text(String string);

    @Test
    public void testEmpty() {

        assertThat(text("").isEmpty()).isTrue();
        assertThat(text("1").isEmpty()).isFalse();
        assertThat(text("12").isEmpty()).isFalse();
        assertThat(text("123").isEmpty()).isFalse();
    }

    @Test
    public void testLength() {

        assertThat(text("").length()).isEqualTo(0L);
        assertThat(text("1").length()).isEqualTo(1L);
        assertThat(text("12").length()).isEqualTo(2L);
        assertThat(text("123").length()).isEqualTo(3L);
        
    }

    @Test
    public void testCharAtEmptyStringThrowsException() {
        
        try {
            text("").charAt(0L);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testCharAtNegativeThrowsException() {
        
        try {
            text("123").charAt(-1L);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testCharAtBoundsThrowsException() {
        
        try {
            text("123").charAt(3L);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
        }
    }
    
    @Test
    public void testCharAt() {

        assertThat(text("123").charAt(0L)).isEqualTo('1');
        assertThat(text("123").charAt(1L)).isEqualTo('2');
        assertThat(text("123").charAt(2L)).isEqualTo('3');
    }

    @Test
    public void testSubstring() {

        assertThat(text("123").substring(0L)).isEqualTo(text("123"));
        assertThat(text("123").substring(1L)).isEqualTo(text("23"));
        assertThat(text("123").substring(2L)).isEqualTo(text("3"));
        assertThat(text("123").substring(3L)).isEqualTo(text(""));
    }

    @Test
    public void testSubstringOneParamArgs() {
        
        final String string = "some\n lines\n \nof text";
        
        final Text text = text(string);

        try {
            text.substring(-1);
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
        }

        try {
            text.substring(string.length() + 1);
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
        }
    }
    
    @Test
    public void testSubstringOneParam() {

        final String string = "some\n lines\n \nof text";
        
        final Text text = text(string);
        
        assertThat(text.substring(0).asString()).isEqualTo(string);
        assertThat(text.substring(4).asString()).isEqualTo(string.substring(4));

        assertThat(text.substring(string.length() - 1).asString()).isEqualTo("t");
        assertThat(text.substring(string.length() - 1).asString()).isEqualTo(string.substring(string.length() - 1));
        
        
        
        assertThat(text.substring(string.length()).asString()).isEqualTo("");
        assertThat(text.substring(string.length()).asString()).isEqualTo(string.substring(string.length()));
    }

    @Test
    public void testSubstringTwoParamsArgs() {
        
        final String string = "some\n lines\n \nof text";

        final Text text = text(string);

        try {
            text.substring(-1, 1);
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
        }

        try {
            text.substring(0, -1);
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
        }

        try {
            text.substring(-1, -1);
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
        }

        try {
            text.substring(1, 0);
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
        }

        try {
            text.substring(0, string.length() + 1);
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
        }
    }
    
    @Test
    public void testSubstringTwoParams() {

        final String string = "some\n lines\n \nof text";
        
        final Text text = text(string);
        
        assertThat(text.substring(0, string.length()).asString()).isEqualTo(string);
        assertThat(text.substring(4, string.length()).asString()).isEqualTo(string.substring(4));

        assertThat(text.substring(4, string.length() - 3).asString()).isEqualTo(string.substring(4, string.length() - 3));

        assertThat(text.substring(string.length() - 1, string.length()).asString()).isEqualTo("t");
        assertThat(text.substring(string.length() - 1, string.length()).asString()).isEqualTo(string.substring(string.length() - 1));
        
        assertThat(text.substring(string.length(), string.length()).asString()).isEqualTo("");
        assertThat(text.substring(string.length(), string.length()).asString()).isEqualTo(string.substring(string.length(), string.length()));
    }

    @Test
    public void testSearchTextForwards() {
        
        final String string = "some\n lines\n \nof text";
        
        final Text text = text(string);
        
        // case sensitive
        assertThat(text.findForward("som",      0L, null, true, false, false)).isEqualTo(0L);
        assertThat(text.findForward("lines",    0L, null, true, false, false)).isEqualTo(6L);
        assertThat(text.findForward("line",     0L, null, true, false, false)).isEqualTo(6L);
        assertThat(text.findForward("text",     0L, null, true, false, false)).isEqualTo(17L);
        assertThat(text.findForward("text",     0L, new TextRange(0L, 17L), true, false, false)).isEqualTo(-1L);
        assertThat(text.findForward("text",     0L, new TextRange(0L, 21L), true, false, false)).isEqualTo(17L);
        assertThat(text.findForward("\n",       0L, null, true, false, false)).isEqualTo(4L);

        assertThat(text.findForward("Som",      0L, null, true, false, false)).isEqualTo(-1L);
        assertThat(text.findForward("Lines",    0L, null, true, false, false)).isEqualTo(-1L);
        assertThat(text.findForward("Line",     0L, null, true, false, false)).isEqualTo(-1L);
        assertThat(text.findForward("\n",       0L, null, true, false, false)).isEqualTo(4L);

        // case insensitive
        assertThat(text.findForward("som",      0L, null, false, false, false)).isEqualTo(0L);
        assertThat(text.findForward("lines",    0L, null, false, false, false)).isEqualTo(6L);
        assertThat(text.findForward("line",     0L, null, false, false, false)).isEqualTo(6L);
        assertThat(text.findForward("\n",       0L, null, false, false, false)).isEqualTo(4L);

        assertThat(text.findForward("Som",      0L, null, false, false, false)).isEqualTo(0L);
        assertThat(text.findForward("Lines",    0L, null, false, false, false)).isEqualTo(6L);
        assertThat(text.findForward("Line",     0L, null, false, false, false)).isEqualTo(6L);
        assertThat(text.findForward("\n",       0L, null, false, false, false)).isEqualTo(4L);

        // Whole words
        assertThat(text.findForward("som",      0L, null, true, false, true)).isEqualTo(-1L);
        assertThat(text.findForward("some",     0L, null, true, false, true)).isEqualTo(0L);
        assertThat(text.findForward("lines",    0L, null, true, false, true)).isEqualTo(6L);
        assertThat(text.findForward("line",     0L, null, true, false, true)).isEqualTo(-1L);
        assertThat(text.findForward("text",     0L, null, true, false, true)).isEqualTo(17L);
        assertThat(text.findForward("text",     10L, null, true, false, true)).isEqualTo(17L);
        assertThat(text.findForward("text",     17L, null, true, false, true)).isEqualTo(17L);
        
        try {
            text.findForward("\n", 0L, null, true, false, true);
            fail("No whitespace in text");
        }
        catch (IllegalArgumentException ex) {
        }
        
        assertThat(text.findForward("text", 0L, null, true, false, true)).isEqualTo(17L);
        
        try {
            text.findForward("of text", 0L, null, true, false, true);
            fail("Expected search text with one word");
        }
        catch (IllegalArgumentException ex) {
        }
        try {
            text.findForward(" ", 0L, null, true, false, true);
            fail("No whitespace in text");
        }
        catch (IllegalArgumentException ex) {
        }

        // Wrapped search
        assertThat(text.findForward("som",      10L, null, true, true, false)).isEqualTo(0L);
        assertThat(text.findForward("lines",    15L, null, true, true, false)).isEqualTo(6L);
        assertThat(text.findForward("line",     15L, null, true, true, false)).isEqualTo(6L);
        assertThat(text.findForward("\n",       17L, null, true, true, false)).isEqualTo(4L);
        assertThat(text.findForward("lines",    20L, null, true, true, false)).isEqualTo(6L);
        assertThat(text.findForward(" text",    17L, null, true, true, false)).isEqualTo(16L);

        assertThat(text.findForward("unknowntext", 10L, null, true, true, false)).isEqualTo(-1L);
    }

    @Test
    public void testSearchTextBackwards() {
        
        final String string = "some\n lines\n \nof text";
        
        final Text text = text(string);
        
        assertThat(string.length()).isEqualTo(21);
        
        // case sensitive
        assertThat(text.findBackward("som",     20L, null, true, false, false)).isEqualTo(0L);
        assertThat(text.findBackward("lines",   20L, null, true, false, false)).isEqualTo(6L);
        assertThat(text.findBackward("line",    15L, null, true, false, false)).isEqualTo(6L);
        assertThat(text.findBackward("text",    17L, null, true, false, false)).isEqualTo(-1L);
        assertThat(text.findBackward("text",    21L, null, true, false, false)).isEqualTo(17L);
        assertThat(text.findBackward("text",    17L, new TextRange(0L, 17L), true, false, false)).isEqualTo(-1L);
        assertThat(text.findBackward("text",    21L, new TextRange(0L, 21L), true, false, false)).isEqualTo(17L);
        assertThat(text.findBackward("\n",      5L,  null, true, false, false)).isEqualTo(4L);

        assertThat(text.findBackward("Som",     20L, null, true, false, false)).isEqualTo(-1L);
        assertThat(text.findBackward("Lines",   20L, null, true, false, false)).isEqualTo(-1L);
        assertThat(text.findBackward("Line",    15L, null, true, false, false)).isEqualTo(-1L);
        assertThat(text.findBackward("\n",      5L,  null, true, false, false)).isEqualTo(4L);

        // case insensitive
        assertThat(text.findBackward("som",     20L, null, false, false, false)).isEqualTo(0L);
        assertThat(text.findBackward("lines",   20L, null, false, false, false)).isEqualTo(6L);
        assertThat(text.findBackward("line",    15L, null, false, false, false)).isEqualTo(6L);
        assertThat(text.findBackward("\n",      5L,  null, false, false, false)).isEqualTo(4L);

        assertThat(text.findBackward("Som",     20L, null, false, false, false)).isEqualTo(0L);
        assertThat(text.findBackward("Lines",   20L, null, false, false, false)).isEqualTo(6L);
        assertThat(text.findBackward("Line",    15L, null, false, false, false)).isEqualTo(6L);
        assertThat(text.findBackward("\n",      5L,  null, false, false, false)).isEqualTo(4L);

        // Whole words
        assertThat(text.findBackward("som",     20L, null, true, false, true)).isEqualTo(-1L);
        assertThat(text.findBackward("some",    20L, null, true, false, true)).isEqualTo(0L);
        assertThat(text.findBackward("lines",   20L, null, true, false, true)).isEqualTo(6L);
        assertThat(text.findBackward("line",    20L, null, true, false, true)).isEqualTo(-1L);
        assertThat(text.findBackward("text",    20L, null, true, false, true)).isEqualTo(-1L);
        assertThat(text.findBackward("text",    21L, null, true, false, true)).isEqualTo(17L);
        
        try {
            text.findBackward("\n", 0L, null, true, false, true);
            fail("No whitespace in text");
        }
        catch (IllegalArgumentException ex) {
        }
        
        assertThat(text.findBackward("text", 21L, null, true, false, true)).isEqualTo(17L);
        
        try {
            text.findBackward("of text", 0L, null, true, false, true);
            fail("Expected search text with one word");
        }
        catch (IllegalArgumentException ex) {
        }
        try {
            text.findBackward(" ", 0L, null, true, false, true);
            fail("No whitespace in text");
        }
        catch (IllegalArgumentException ex) {
        }

        // Wrapped search
        assertThat(text.findBackward("som",     0L, null, true, true, false)).isEqualTo(0L);
        assertThat(text.findBackward("lines",   3L, null, true, true, false)).isEqualTo(6L);
        assertThat(text.findBackward("line",    3L, null, true, true, false)).isEqualTo(6L);
        assertThat(text.findBackward("\n",      3L, null, true, true, false)).isEqualTo(13L);
        assertThat(text.findBackward("lines",   5L, null, true, true, false)).isEqualTo(6L);
        assertThat(text.findBackward(" text",   10L, null, true, true, false)).isEqualTo(16L);

        assertThat(text.findBackward("unknowntext", 10L, null, true, true, false)).isEqualTo(-1L);
    }

    @Test
    public void testStartsWith() {
        assertThat(text("123").startsWith("1234")).isFalse();
        assertThat(text("123").startsWith("123")).isTrue();
        assertThat(text("123").startsWith("124")).isFalse();
        assertThat(text("123").startsWith("12")).isTrue();
        assertThat(text("123").startsWith("1")).isTrue();
        assertThat(text("123").startsWith("")).isTrue();
    }

    @Test
    public void testEndsWith() {
        assertThat(text("123").endsWith("1234")).isFalse();
        assertThat(text("123").endsWith("123")).isTrue();
        assertThat(text("123").endsWith("124")).isFalse();
        assertThat(text("123").endsWith("23")).isTrue();
        assertThat(text("123").endsWith("3")).isTrue();
        assertThat(text("123").endsWith("")).isTrue();
    }

    @Test
    public void testTrailingWithMoreThanLengthThrowsException() {

        try {
            text("123").trailing(4L);
            
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }

    @Test
    public void testTrailingWithNegativeLengthThrowsException() {

        try {
            text("123").trailing(-1L);
            
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }

    @Test
    public void testTrailing() {
        
        assertThat(text("123").trailing(0L)).isEqualTo(text(""));
        assertThat(text("123").trailing(1L)).isEqualTo(text("3"));
        assertThat(text("123").trailing(2L)).isEqualTo(text("23"));
        assertThat(text("123").trailing(3L)).isEqualTo(text("123"));
    }

    @Test
    public void testInitialWithMoreThanLengthThrowsException() {

        try {
            text("123").initial(4L);
            
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }

    @Test
    public void testInitialWithNegativeLengthThrowsException() {

        try {
            text("123").initial(-1L);
            
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }

    @Test
    public void testInitial() {
        
        assertThat(text("123").initial(0L)).isEqualTo(text(""));
        assertThat(text("123").initial(1L)).isEqualTo(text("1"));
        assertThat(text("123").initial(2L)).isEqualTo(text("12"));
        assertThat(text("123").initial(3L)).isEqualTo(text("123"));
    }

    @Test
    public void testSubstringMatchesWithMoreThanLengthThrowsException() {

        try {
            text("123").substringMatches(3L, "456");
            
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }

        try {
            text("123").substringMatches(4L, "456");
            
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }

    @Test
    public void testSubstringMatchesWithNegativeIndexThrowsException() {

        try {
            text("123").substringMatches(-1L, "01");
            
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }

    @Test
    public void testSubstringMatches() {
        
        assertThat(text("123").substringMatches(0L, "")).isTrue();
        assertThat(text("123").substringMatches(0L, "1")).isTrue();
        assertThat(text("123").substringMatches(0L, "12")).isTrue();
        assertThat(text("123").substringMatches(0L, "123")).isTrue();
        assertThat(text("123").substringMatches(0L, "1234")).isFalse();
        assertThat(text("123").substringMatches(0L, "2")).isFalse();
        assertThat(text("123").substringMatches(0L, "212")).isFalse();

        assertThat(text("123").substringMatches(1L, "")).isTrue();
        assertThat(text("123").substringMatches(1L, "2")).isTrue();
        assertThat(text("123").substringMatches(1L, "23")).isTrue();
        assertThat(text("123").substringMatches(1L, "234")).isFalse();
        assertThat(text("123").substringMatches(1L, "3")).isFalse();
        assertThat(text("123").substringMatches(1L, "32")).isFalse();

        assertThat(text("123").substringMatches(2L, "")).isTrue();
        assertThat(text("123").substringMatches(2L, "3")).isTrue();
        assertThat(text("123").substringMatches(2L, "34")).isFalse();
    }

    @Test
    public void testSubstringMatchesWithNegativeCountThrowsException() {

        try {
            text("123").substringMatches(1L, "456", -1);
            
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }

    @Test
    public void testSubstringMatchesWithCountWithMoreThanCountThrowsException() {

        try {
            text("12345").substringMatches(1L, "456", 4);
            
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }

        try {
            text("12345").substringMatches(1L, "456", 5);
            
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }

    @Test
    public void testSubstringMatchesWithCount() {

        assertThat(text("123").substringMatches(0L, "", 0)).isTrue();
        assertThat(text("123").substringMatches(0L, "1", 1)).isTrue();

        assertThat(text("123").substringMatches(0L, "12", 0)).isTrue();
        assertThat(text("123").substringMatches(0L, "12", 1)).isTrue();
        assertThat(text("123").substringMatches(0L, "12", 2)).isTrue();

        assertThat(text("123").substringMatches(0L, "123", 0)).isTrue();
        assertThat(text("123").substringMatches(0L, "123", 1)).isTrue();
        assertThat(text("123").substringMatches(0L, "123", 2)).isTrue();
        assertThat(text("123").substringMatches(0L, "123", 3)).isTrue();

        assertThat(text("123").substringMatches(0L, "1234", 0)).isTrue();
        assertThat(text("123").substringMatches(0L, "1234", 1)).isTrue();
        assertThat(text("123").substringMatches(0L, "1234", 2)).isTrue();
        assertThat(text("123").substringMatches(0L, "1234", 3)).isTrue();
        assertThat(text("123").substringMatches(0L, "1234", 4)).isFalse();

        assertThat(text("123").substringMatches(0L, "2", 0)).isTrue();
        assertThat(text("123").substringMatches(0L, "2", 1)).isFalse();
        
        assertThat(text("123").substringMatches(0L, "212", 0)).isTrue();
        assertThat(text("123").substringMatches(0L, "212", 1)).isFalse();
        assertThat(text("123").substringMatches(0L, "212", 2)).isFalse();
        assertThat(text("123").substringMatches(0L, "212", 3)).isFalse();

        assertThat(text("123").substringMatches(1L, "", 0)).isTrue();
        
        assertThat(text("123").substringMatches(1L, "2", 0)).isTrue();
        assertThat(text("123").substringMatches(1L, "2", 1)).isTrue();

        assertThat(text("123").substringMatches(1L, "23", 0)).isTrue();
        assertThat(text("123").substringMatches(1L, "23", 1)).isTrue();
        assertThat(text("123").substringMatches(1L, "23", 2)).isTrue();
        
        assertThat(text("123").substringMatches(1L, "234", 0)).isTrue();
        assertThat(text("123").substringMatches(1L, "234", 1)).isTrue();
        assertThat(text("123").substringMatches(1L, "234", 2)).isTrue();
        assertThat(text("123").substringMatches(1L, "234", 3)).isFalse();
        
        assertThat(text("123").substringMatches(1L, "3", 0)).isTrue();
        assertThat(text("123").substringMatches(1L, "3", 1)).isFalse();

        assertThat(text("123").substringMatches(1L, "32", 0)).isTrue();
        assertThat(text("123").substringMatches(1L, "32", 1)).isFalse();
        assertThat(text("123").substringMatches(1L, "32", 2)).isFalse();

        assertThat(text("123").substringMatches(2L, "", 0)).isTrue();
        
        assertThat(text("123").substringMatches(2L, "3", 0)).isTrue();
        assertThat(text("123").substringMatches(2L, "3", 1)).isTrue();

        assertThat(text("123").substringMatches(2L, "34", 0)).isTrue();
        assertThat(text("123").substringMatches(2L, "34", 1)).isTrue();
        assertThat(text("123").substringMatches(2L, "34")).isFalse();
    }
}
