package dev.nimbler.ide.model.text;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

import dev.nimbler.ide.util.ui.text.StringText;
import dev.nimbler.ide.util.ui.text.Text;

public class TextEditMergeTest {

    @Test
    public void testMergeNonOverlappingAddedThrowsException() {
        
        final TextAdd add1 = new TextAdd(0, text("123"));
        final TextAdd add2 = new TextAdd(4, text("567"));
    
        try {
            merge(add1, add2);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testMergeConsecutiveAdds() {
        
        final TextAdd add1 = new TextAdd(0L, text("123"));
        final TextAdd add2 = new TextAdd(3L, text("456"));
    
        final TextEdit merged = merge(add1, add2);
        
        assertThat(merged).isInstanceOf(TextAdd.class);
        
        final TextAdd mergedAdd = (TextAdd)merged;
        
        assertThat(mergedAdd.getAddedText()).isEqualTo(text("123456"));
        assertThat(mergedAdd.getNewLength()).isEqualTo("123456".length());
        
        assertThat(mergedAdd.getOldText()).isEqualTo(Text.EMPTY_TEXT);
        assertThat(mergedAdd.getOldLength()).isEqualTo(0L);
    }

    @Test
    public void testMergeOverlapAtMinus1Adds() {

        checkMergeAddsThrowsException(3L, "123", 2L, "456");
    }

    @Test
    public void testMergeOverlapAt0Adds() {

        checkMergeAdds("456123", 0L, "123", 0L, "456");
    }

    @Test
    public void testMergeOverlapAt1Adds() {
        
        checkMergeAdds("145623", 0L, "123", 1L, "456");
    }

    @Test
    public void testMergeOverlapAt2Adds() {

        checkMergeAdds("124563", 0L, "123", 2L, "456");
    }
    
    private void checkMergeAdds(
            String expected,
            long startPos1, String text1,
            long startPos2, String text2) {
        
        final TextAdd add1 = new TextAdd(startPos1, text(text1));
        final TextAdd add2 = new TextAdd(startPos2, text(text2));
    
        final TextEdit merged = merge(add1, add2);
        
        checkAdd(merged, expected, startPos1);
    }

    private void checkMergeAddsThrowsException(
            long startPos1, String text1,
            long startPos2, String text2) {
        
        final TextAdd add1 = new TextAdd(startPos1, text(text1));
        final TextAdd add2 = new TextAdd(startPos2, text(text2));
    
        try {
            merge(add1, add2);
        
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }

    @Test
    public void testMergeAddAndReplaceAt0() {

        checkMergeAddAndReplace(
                "4523",
                0L, "123",
                0L, 1L, "1", "45");
    }

    @Test
    public void testMergeAddAndReplaceAt1() {

        checkMergeAddAndReplace(
                "1453",
                0L, "123",
                1L, 1L, "2", "45");
    }

    @Test
    public void testMergeAddAndReplaceAt2() {

        checkMergeAddAndReplace(
                "1245",
                0L, "123",
                2L, 1L, "3", "45");
    }

    @Test
    public void testMergeAddAndReplaceBeyondAt0() {

        checkMergeAddAndReplaceBeyond(
                "1234",
                "45",
                0L, "123",
                0L, 4L, "1234", "45");
    }

    @Test
    public void testMergeAddAndReplaceBeyondAt1() {

        checkMergeAddAndReplaceBeyond(
                "1234",
                "145",
                0L, "123",
                1L, 3L, "234", "45");
    }

    @Test
    public void testMergeAddAndReplaceBeyondAt2() {

        checkMergeAddAndReplaceBeyond(
                "1234",
                "1245",
                0L, "123",
                2L, 2L, "34", "45");
    }

    private void checkMergeAddAndReplace(
            String expected,
            long startPos1, String text1,
            long startPos2, long toReplaceCount, String toReplace, String updated) {
        
        assertThat(toReplaceCount).isEqualTo(toReplace.length());
        
        final TextAdd add = new TextAdd(0L, text(text1));
        final TextReplace replace = new TextReplace(
                    startPos2, toReplace.length(),
                    text(toReplace), text(updated));

        final TextEdit merged = merge(add, replace);

        checkAdd(merged, expected, startPos1);
    }
    
    private void checkAdd(TextEdit merged, String expected, long expectedStartPos) {
        
        assertThat(merged).isInstanceOf(TextAdd.class);

        final TextAdd mergedAdd = (TextAdd)merged;
        
        assertThat(mergedAdd.getAddedText()).isEqualTo(text(expected));
        assertThat(mergedAdd.getNewLength()).isEqualTo(expected.length());
        
        assertThat(mergedAdd.getOldText()).isEqualTo(Text.EMPTY_TEXT);
        assertThat(mergedAdd.getOldLength()).isEqualTo(0L);
        
        assertThat(mergedAdd.getStartPos()).isEqualTo(expectedStartPos);
    }

    private void checkMergeAddAndReplaceBeyond(
            String oldReplaceText,
            String expected,
            long startPos1, String text1,
            long startPos2, long toReplaceCount, String toReplace, String updated) {
        
        assertThat(toReplaceCount).isEqualTo(toReplace.length());
        
        final TextAdd add = new TextAdd(0L, text(text1));
        final TextReplace replace = new TextReplace(
                    startPos2, toReplace.length(),
                    text(toReplace), text(updated));

        final TextEdit merged = merge(add, replace);

        checkReplace(merged, oldReplaceText, expected, startPos1);
    }

    private void checkReplace(TextEdit merged, String oldText, String expected, long expectedStartPos) {
        
        assertThat(expectedStartPos).isNotNegative();
        
        assertThat(merged).isInstanceOf(TextReplace.class);
        
        assertThat(oldText).isNotEmpty();
        assertThat(expected).isNotEmpty();

        final TextReplace mergedReplace = (TextReplace)merged;
        
        assertThat(mergedReplace.getNewText()).isEqualTo(text(expected));
        assertThat(mergedReplace.getNewLength()).isEqualTo(expected.length());
        
        assertThat(mergedReplace.getOldText()).isEqualTo(text(oldText));
        assertThat(mergedReplace.getOldLength()).isEqualTo(oldText.length());
        
        assertThat(mergedReplace.getStartPos()).isEqualTo(expectedStartPos);
    }

    @Test
    public void testMergeAddAndRemoveAll() {

        final TextAdd add = new TextAdd(0L, text("123"));
        final TextRemove remove = new TextRemove(0L, 3L, text("123"));

        final TextEdit merged = merge(add, remove);

        assertThat(merged).isNull();
    }

    @Test
    public void testMergeAddAndRemove1At0() {
        
        checkMergeAddAndRemove(
                "23",
                0L, "123",
                0L, 1L,
                "1");
    }

    @Test
    public void testMergeAddAndRemove2At0() {
        
        checkMergeAddAndRemove(
                "3",
                0L, "123",
                0L, 2L,
                "12");
    }

    @Test
    public void testMergeAddAndRemove1At1() {
        
        checkMergeAddAndRemove(
                "13",
                0L, "123",
                1L, 1L,
                "2");
    }

    @Test
    public void testMergeAddAndRemove2At1() {
        
        checkMergeAddAndRemove(
                "1",
                0L, "123",
                1L, 2L,
                "23");
    }

    @Test
    public void testMergeAddAndRemoveBeyond1() {
        
        checkMergeAddAndRemoveBeyond(
                "4",
                0L, "123",
                0L, 4L, "1234");
    }

    @Test
    public void testMergeAddAndRemoveBeyond2() {
        
        checkMergeAddAndRemoveBeyond(
                "45",
                0L, "123",
                0L, 5L, "12345");
    }

    @Test
    public void testMergeAddAndRemoveBeyondReplace1() {
        
        checkMergeAddAndRemoveBeyondReplace(
                "45",
                "1",
                0L, "123",
                1L, 4L, "2345");
    }

    @Test
    public void testMergeAddAndRemoveBeyondReplace2() {
        
        checkMergeAddAndRemoveBeyondReplace(
                "45",
                "12",
                0L, "123",
                2L, 3L, "345");
    }

    @Test
    public void testMergeAddAndRemoveBeyondReplace3ThrowsException() {
        
        final TextAdd add = new TextAdd(0L, text("123"));
        final TextRemove remove = new TextRemove(3L, 2L, text("45"));

        try {
            merge(add, remove);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
        }
    }

    private void checkMergeAddAndRemove(
            String expectedRemaining,
            long startPos1, String text1,
            long startPos2, long toRemoveCount, String toRemove) {

        assertThat(startPos2 + toRemoveCount).isLessThanOrEqualTo(startPos1 + text1.length());
        assertThat(toRemoveCount).isEqualTo(toRemove.length());
        
        final TextAdd add = new TextAdd(startPos1, text(text1));
        final TextRemove remove = new TextRemove(startPos2, toRemoveCount, text(toRemove));

        final TextEdit merged = merge(add, remove);

        checkAdd(merged, expectedRemaining, startPos1);
    }

    private void checkMergeAddAndRemoveBeyond(
            String expectedRemoved,
            long startPos1, String text1,
            long startPos2, long toRemoveCount, String toRemove) {
        
        assertThat(toRemoveCount).isEqualTo(toRemove.length());

        final TextAdd add = new TextAdd(startPos1, text(text1));
        final TextRemove remove = new TextRemove(startPos2, toRemoveCount, text(toRemove));

        final TextEdit merged = merge(add, remove);

        checkRemove(merged, expectedRemoved, startPos2);
    }

    private void checkRemove(TextEdit merged, String expected, long expectedStartPos) {
        
        assertThat(merged).isInstanceOf(TextRemove.class);

        final TextRemove mergedRemove = (TextRemove)merged;
        
        assertThat(mergedRemove.getOldText()).isEqualTo(text(expected));
        assertThat(mergedRemove.getOldLength()).isEqualTo(expected.length());
        
        assertThat(mergedRemove.getNewText()).isEqualTo(Text.EMPTY_TEXT);
        assertThat(mergedRemove.getNewLength()).isEqualTo(0L);
        
        assertThat(mergedRemove.getStartPos()).isEqualTo(expectedStartPos);
    }

    private void checkMergeAddAndRemoveBeyondReplace(
            String oldText,
            String updated,
            long startPos1, String text1,
            long startPos2, long toRemoveCount, String toRemove) {

        assertThat(toRemoveCount).isEqualTo(toRemove.length());

        final TextAdd add = new TextAdd(startPos1, text(text1));
        final TextRemove remove = new TextRemove(startPos2, toRemoveCount, text(toRemove));

        final TextEdit merged = merge(add, remove);

        checkReplace(merged, oldText, updated, startPos1);
    }

    private static TextEdit merge(TextEdit textEdit1, TextEdit textEdit2) {
        
        return TextEditMerge.merge(
                textEdit1, textEdit1.getStartPos(),
                textEdit2, textEdit2.getStartPos());
    }

    private static Text text(String string) {
        return new StringText(string);
    }
}
