package dev.nimbler.ide.model.text;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

import dev.nimbler.ide.util.ui.text.Text;

public class TextEditMergeHelperMergeTest extends BaseTextEditMergeHelperTest {

    @Test
    public void testMergeNonOverlappingAddedThrowsException() {
        
        final Edit<TextAdd> add1 = add(0, "123");
        final Edit<TextAdd> add2 = add(4, "567");
    
        try {
            merge(add1, add2);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testMergeConsecutiveAdds() {
        
        final Edit<TextAdd> add1 = add(0L, "123");
        final Edit<TextAdd> add2 = add(3L, "456");
    
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
        
        final Edit<TextAdd> add1 = add(startPos1, text1);
        final Edit<TextAdd> add2 = add(startPos2, text2);
    
        final TextEdit merged = merge(add1, add2);
        
        checkAdd(merged, expected, startPos1);
    }

    private void checkMergeAddsThrowsException(
            long startPos1, String text1,
            long startPos2, String text2) {
        
        final Edit<TextAdd> add1 = add(startPos1, text1);
        final Edit<TextAdd> add2 = add(startPos2, text2);
    
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
        
        
        final Edit<TextAdd> add = add(0L, text1);

        assertThat(toReplaceCount).isEqualTo(toReplace.length());

        final Edit<TextReplace> replace = replace(startPos2, toReplace, updated);

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
    }

    private void checkMergeAddAndReplaceBeyond(
            String oldReplaceText,
            String expected,
            long startPos1, String text1,
            long startPos2, long toReplaceCount, String toReplace, String updated) {
        
        
        final Edit<TextAdd> add = add(0L, text1);

        assertThat(toReplaceCount).isEqualTo(toReplace.length());
        
        final Edit<TextReplace> replace = replace(startPos2, toReplace, updated);

        final TextEdit merged = merge(add, replace);

        checkReplace(merged, oldReplaceText, expected);
    }

    private void checkReplace(TextEdit merged, String oldText, String expected) {
        
        assertThat(merged).isInstanceOf(TextReplace.class);
        
        assertThat(oldText).isNotEmpty();
        assertThat(expected).isNotEmpty();

        final TextReplace mergedReplace = (TextReplace)merged;
        
        assertThat(mergedReplace.getNewText()).isEqualTo(text(expected));
        assertThat(mergedReplace.getNewLength()).isEqualTo(expected.length());
        
        assertThat(mergedReplace.getOldText()).isEqualTo(text(oldText));
        assertThat(mergedReplace.getOldLength()).isEqualTo(oldText.length());
    }

    @Test
    public void testMergeAddAndRemoveAll() {

        final Edit<TextAdd> add = add(0L, "123");
        final Edit<TextRemove> remove = remove(0L, "123");

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
                "1", // Expected remaining of Add
                0L, "123", // Original Add
                1L, 2L, // Remove 2 characters from pos 1
                "23"); // Removed text
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
                "45", // resulting replaced oldText (out of text beyond add, since add is merged)
                "1",  // resulting replace updated text, since "45" is removed from text beyond add
                0L, "123", // initial TextAdd
                
                // TextRemove to be merged, replaces "45" out of text beyond add,
                // removes "23" of added so "1" remains
                1L, 4L, "2345");
    }

    @Test
    public void testMergeAddAndRemoveBeyondReplace2() {
        
        checkMergeAddAndRemoveBeyondReplace(
                "45", // resulting replaced oldText (out of text beyond add)
                "12", // resulting replace updated text
                0L, "123",
                2L, 3L, "345");
    }

    @Test
    public void testMergeAddAndRemoveBeyondReplace3ThrowsException() {
        
        final Edit<TextAdd> add = add(0L, "123");
        final Edit<TextRemove> remove = remove(3L, "45");

        try {
            merge(add, remove);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
        }
    }
    
    @Test
    public void testMergeReplaceAndAddBeforeStartPosThrowsException() {
        
        final Edit<TextReplace> replace = replace(1, "123", "45");
        final Edit<TextAdd> add = add(0, "34");
        
        try {
            merge(replace, add);
        
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }

    @Test
    public void testMergeReplaceAndAddAtSame() {
        
        final String replaced = "123";
        final Edit<TextReplace> replace = replace(1, replaced, "45");

        final Edit<TextAdd> add = add(1, "34");
        
        final TextReplace merged = (TextReplace)merge(replace, add);
        
        assertThat(merged.getOldLength()).isEqualTo(replaced.length());
        assertThat(merged.getOldText()).isEqualTo(text(replaced));

        assertThat(merged.getNewLength()).isEqualTo(4);
        assertThat(merged.getNewText()).isEqualTo(text("3445"));
    }

    @Test
    public void testMergeReplaceAndAddAtWithin() {
        
        final String replaced = "123";
        final Edit<TextReplace> replace = replace(1, replaced, "45");
        
        final Edit<TextAdd> add = add(2, "34");
        
        final TextReplace merged = (TextReplace)merge(replace, add);
        
        assertThat(merged.getOldLength()).isEqualTo(replaced.length());
        assertThat(merged.getOldText()).isEqualTo(text(replaced));

        assertThat(merged.getNewLength()).isEqualTo(4);
        assertThat(merged.getNewText()).isEqualTo(text("4345"));
    }

    @Test
    public void testMergeReplaceAndAddAtEnd() {
        
        final String replaced = "123";
        final Edit<TextReplace> replace = replace(1, replaced, "45");

        final Edit<TextAdd> add = add(3, "34");
        
        final TextReplace merged = (TextReplace)merge(replace, add);
        
        assertThat(merged.getOldLength()).isEqualTo(replaced.length());
        assertThat(merged.getOldText()).isEqualTo(text(replaced));

        assertThat(merged.getNewLength()).isEqualTo(4);
        assertThat(merged.getNewText()).isEqualTo(text("4534"));
    }

    @Test
    public void testMergeReplaceAndAddAfterStartPosPlusOldLengthThrowsException() {
        
        final String replaced = "123";
        final Edit<TextReplace> replace = replace(1, replaced, "45");

        final Edit<TextAdd> add = add(4, "34");
        
        try {
            merge(replace, add);
        
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }

    @Test
    public void testMergeReplaceAndReplaceBeforeStartPosThrowsException() {
        
        final String update1 = "123";
        final Edit<TextReplace> replace1 = replace(1, update1, "45");
        
        final String replace2OldText = "3" + update1.substring(0, 1);
        
        final Edit<TextReplace> replace2 = replace(0, replace2OldText, "34");
        
        try {
            merge(replace1, replace2);
        
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }
    
    @Test
    public void testMergeReplaceAndReplaceAtSameWithMismatchThrowsException() {
        
        // Effectively tries to replace "45" with another replace with old-text "34" at same startPos
        final String update1 = "123";
        final Edit<TextReplace> replace1 = replace(1, update1, "45");
        
        final String replace2OldText = "34";
        final Edit<TextReplace> replace2 = replace(1, replace2OldText, "23");
        
        try {
            merge(replace1, replace2);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }

    @Test
    public void testMergeReplaceAndReplaceAtSameReplace1LongerThanUpdate() {
        
        final String replaceOldText1 = "12";
        final Edit<TextReplace> replace1 = replace(1, replaceOldText1, "345");

        final String update2 = "56";
        final Edit<TextReplace> replace2 = replace(1L, "34", update2);
        
        final TextReplace merged = (TextReplace)merge(replace1, replace2);
        
        assertThat(merged.getOldLength()).isEqualTo(replaceOldText1.length());
        assertThat(merged.getOldText()).isEqualTo(text(replaceOldText1));

        assertThat(merged.getNewLength()).isEqualTo(3);
        assertThat(merged.getNewText()).isEqualTo(text("565"));
    }

    @Test
    public void testMergeReplaceAndReplaceAtSameReplacedSameLengthAsUpdate() {
        
        final String replacedOldText1 = "12"; // initial text e.g. 012345, replacing at pos 1
        final Edit<TextReplace> replace1 = replace(1, replacedOldText1, "45");

        final String update2 = "56";
        final Edit<TextReplace> replace2 = replace(1, "45", update2);
        
        final TextReplace mergeReplace = (TextReplace)merge(replace1, replace2);
        
        assertThat(mergeReplace.getOldLength()).isEqualTo(2);
        assertThat(mergeReplace.getOldText()).isEqualTo(text("12"));

        assertThat(mergeReplace.getNewLength()).isEqualTo(2);
        assertThat(mergeReplace.getNewText()).isEqualTo(text("56"));
    }

    @Test
    public void testMergeReplaceAndReplaceAtSameReplace2LongerThanUpdate() {
        
        final String replaced1OldText = "12";
        final Edit<TextReplace> replace1 = replace(1, replaced1OldText, "45");

        final String update2 = "345";
        final Edit<TextReplace> replace2 = replace(1, "45", update2);
        
        final TextReplace mergeReplace = (TextReplace)merge(replace1, replace2);
        
        assertThat(mergeReplace.getOldLength()).isEqualTo(2);
        assertThat(mergeReplace.getOldText()).isEqualTo(text("12"));

        assertThat(mergeReplace.getNewLength()).isEqualTo(3);
        assertThat(mergeReplace.getNewText()).isEqualTo(text("345"));
    }

    @Test
    public void testMergeReplaceAndReplaceAtWithinMismatchThrowsException() {

        final String replace1OldText = "123";
        final Edit<TextReplace> replace1 = replace(1, replace1OldText, "456");
        
        final String replace2OldText = "34";
        final Edit<TextReplace> replace2 = replace(2, replace2OldText, "12");
        
        try {
            merge(replace1, replace2);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }

    @Test
    public void testMergeReplaceWithReplaceAfterEndIndexThrowsException() {

        final String replace1OldText = "123";
        final Edit<TextReplace> replace1 = replace(1, replace1OldText, "45");
        
        final String replaceOldText2 = "34";
        final Edit<TextReplace> replace2 = replace(3, replaceOldText2, "12");
        
        try {
            merge(replace1, replace2);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }

    @Test
    public void testMergeReplaceAndReplaceWithinSameAsUpdateMismatchThrowsException() {
        
        final String replace1OldText = "12";
        final Edit<TextReplace> replace1 = replace(1, replace1OldText, "345");

        final String replace2OldText = "56";
        final Edit<TextReplace> replace2 = replace(2, replace2OldText, "12");
        
        try {
            merge(replace1, replace2);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }
    
    @Test
    public void testMergeReplaceAndReplaceWithinSameAsUpdate1() {
        
        // write "25" at pos 1, replace it with "345", then replace "45" with "56", same as replace "25" with "356"
        
        final String replace1OldText = "25";
        final Edit<TextReplace> replace1 = replace(1, replace1OldText, "345");

        final String replace2OldText = "45";
        final Edit<TextReplace> replace2 = replace(2, replace2OldText, "56");
        
        final TextReplace mergeReplace = (TextReplace)merge(replace1, replace2);
        
        assertThat(mergeReplace.getOldLength()).isEqualTo(2);
        assertThat(mergeReplace.getOldText()).isEqualTo(text("25"));

        assertThat(mergeReplace.getNewLength()).isEqualTo(3);
        assertThat(mergeReplace.getNewText()).isEqualTo(text("356"));
    }

    @Test
    public void testMergeReplaceAndReplaceWithinLessThanUpdateMismatchThrowsException() {
        
        final String replace1OldText = "12";
        final Edit<TextReplace> replace1 = replace(1, replace1OldText, "3456");

        final String replace2OldText = "56";
        final Edit<TextReplace> replace2 = replace(2, replace2OldText, "123");
        
        try {
            merge(replace1, replace2);
        
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }

    @Test
    public void testMergeReplaceAndReplaceWithinLessThanUpdate() {
        
        // write "12", replace it with "3456", then replace "45" with "23", same as replace "12" with "3236"
        
        final String replace1OldText = "12";
        final Edit<TextReplace> replace1 = replace(1, replace1OldText, "3456");

        final String replace2OldText = "45";
        final Edit<TextReplace> replace2 = replace(2, replace2OldText, "23");
        
        final TextReplace mergeReplace = (TextReplace)merge(replace1, replace2);
        
        assertThat(mergeReplace.getOldLength()).isEqualTo(2);
        assertThat(mergeReplace.getOldText()).isEqualTo(text("12"));

        assertThat(mergeReplace.getNewLength()).isEqualTo(4);
        assertThat(mergeReplace.getNewText()).isEqualTo(text("3236"));
    }

    @Test
    public void testMergeReplaceAndReplaceBeyondUpdateMismatchThrowsException() {
        
        final String replace1OldText = "12";
        final Edit<TextReplace> replace1 = replace(1, replace1OldText, "34");

        final String replace2OldText = "56";
        final Edit<TextReplace> replace2 = replace(2, replace2OldText, "123");
        
        try {
            merge(replace1, replace2);
        
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }
    
    @Test
    public void testMergeReplaceAndReplaceBeyondUpdate() {
        
        // write "125", replace "12" with "34", then replace "45" with "12", same as replace "125" with "312"
        
        final String replace1OldText = "12";
        final Edit<TextReplace> replace1 = replace(1, replace1OldText, "34");

        final String replace2OldText = "45";
        final Edit<TextReplace> replace2 = replace(2, replace2OldText, "56");
        
        final TextReplace mergeReplace = (TextReplace)merge(replace1, replace2);
        
        assertThat(mergeReplace.getOldLength()).isEqualTo(3);
        assertThat(mergeReplace.getOldText()).isEqualTo(text("125"));

        assertThat(mergeReplace.getNewLength()).isEqualTo(3);
        assertThat(mergeReplace.getNewText()).isEqualTo(text("356"));
    }
    
    @Test
    public void testMergeReplaceAndRemoveBeforeStartPosThrowsException() {
        
        final String replaced = "123";
        
        final Edit<TextReplace> replace = replace(1, replaced, "45");
        
        final String removed = "3" + replaced.substring(0, 1);
        
        final Edit<TextRemove> remove = remove(0, removed);
        
        try {
            merge(replace, remove);
        
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }
    
    @Test
    public void testMergeReplaceAndRemoveAtSameWithMismatchThrowsException() {
        
        final String replaced = "123";
        final Edit<TextReplace> replace = replace(1, replaced, "45");
        
        final String removed = "34";
        final Edit<TextRemove> remove = remove(1, removed);
        
        try {
            merge(replace, remove);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }

    @Test
    public void testMergeReplaceAndRemoveAtSameReplacedLongerThanUpdate() {
        
        final String replaced = "12";
        final Edit<TextReplace> replace = replace(1, replaced, "345");

        final String removed = "34";
        final Edit<TextRemove> remove = remove(1, removed);
        
        final TextReplace merged = (TextReplace)merge(replace, remove);
        
        assertThat(merged.getOldLength()).isEqualTo(replaced.length());
        assertThat(merged.getOldText()).isEqualTo(text(replaced));

        assertThat(merged.getNewLength()).isEqualTo(1);
        assertThat(merged.getNewText()).isEqualTo(text("5"));
    }

    @Test
    public void testMergeReplaceAndRemoveAtSameRemovedSameLengthAsUpdate() {
        
        final String replaced = "12";
        final Edit<TextReplace> replace = replace(1, replaced, "34");

        final String removed = "34";
        final Edit<TextRemove> remove = remove(1, removed);
        
        final TextRemove mergeRemove = (TextRemove)merge(replace, remove);
        
        assertThat(mergeRemove.getOldLength()).isEqualTo(2);
        assertThat(mergeRemove.getOldText()).isEqualTo(text("12"));
    }

    @Test
    public void testMergeReplaceAndRemoveAtSameRemovedLongerThanUpdate() {
        
        final String replaced = "12";
        final Edit<TextReplace> replace = replace(1, replaced, "34");

        final String removed = "345";
        final Edit<TextRemove> remove = remove(1, removed);
        
        final TextRemove mergeRemove = (TextRemove)merge(replace, remove);
        
        assertThat(mergeRemove.getOldLength()).isEqualTo(3);
        assertThat(mergeRemove.getOldText()).isEqualTo(text("125"));
    }

    @Test
    public void testMergeReplaceAndRemoveAtWithinMismatchThrowsException() {

        final String replaced = "123";
        final Edit<TextReplace> replace = replace(1, replaced, "456");
        
        final String removed = "34";
        final Edit<TextRemove> remove = remove(2, removed);
        
        try {
            merge(replace, remove);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }

    @Test
    public void testMergeReplaceWithRemoveAfterEndIndexThrowsException() {

        final String replaced = "123";
        final Edit<TextReplace> replace = replace(1, replaced, "45");
        
        final String removed = "34";
        final Edit<TextRemove> remove = remove(3, removed);
        
        try {
            merge(replace, remove);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }

    @Test
    public void testMergeReplaceAndRemoveWithinSameAsUpdateMismatchThrowsException() {
        
        // write "12", replace it with "345", then remove "45", same as replace "12" with "3"
        
        final String replaced = "12";
        final Edit<TextReplace> replace = replace(1, replaced, "345");

        final String removed = "56";
        final Edit<TextRemove> remove = remove(2, removed);
        
        try {
            merge(replace, remove);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }
    
    @Test
    public void testMergeReplaceAndRemoveWithinSameAsUpdate() {
        
        // write "12", replace it with "345", then remove "45", same as replace "12" with "3"
        
        final String replaced = "12";
        final Edit<TextReplace> replace = replace(1, replaced, "345");

        final String removed = "45";
        final Edit<TextRemove> remove = remove(2, removed);
        
        final TextReplace mergeReplace = (TextReplace)merge(replace, remove);
        
        assertThat(mergeReplace.getOldLength()).isEqualTo(2);
        assertThat(mergeReplace.getOldText()).isEqualTo(text("12"));

        assertThat(mergeReplace.getNewLength()).isEqualTo(1);
        assertThat(mergeReplace.getNewText()).isEqualTo(text("3"));
    }

    @Test
    public void testMergeReplaceAndRemoveWithinLessThanUpdateMismatchThrowsException() {
        
        // write "12", replace it with "3456", then remove "45", same as replace "12" with "36"
        
        final String replaced = "12";
        final Edit<TextReplace> replace = replace(1, replaced, "3456");

        final String removed = "56";
        final Edit<TextRemove> remove = remove(2, removed);
        
        try {
            merge(replace, remove);
        
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }

    @Test
    public void testMergeReplaceAndRemoveWithinLessThanUpdate() {
        
        // write "12", replace it with "3456", then remove "45", same as replace "12" with "36"
        
        final String replaced = "12";
        final Edit<TextReplace> replace = replace(1, replaced, "3456");

        final String removed = "45";
        final Edit<TextRemove> remove = remove(2, removed);
        
        final TextReplace mergeReplace = (TextReplace)merge(replace, remove);
        
        assertThat(mergeReplace.getOldLength()).isEqualTo(2);
        assertThat(mergeReplace.getOldText()).isEqualTo(text("12"));

        assertThat(mergeReplace.getNewLength()).isEqualTo(2);
        assertThat(mergeReplace.getNewText()).isEqualTo(text("36"));
    }

    @Test
    public void testMergeReplaceAndRemoveBeyondUpdateMismatchThrowsException() {
        
        // write "125", replace it "12" with "34", then remove "45", same as replace "125" with "3"
        
        final String replaced = "12";
        final Edit<TextReplace> replace = replace(1, replaced, "34");

        final String removed = "56";
        final Edit<TextRemove> remove = remove(2, removed);
        
        try {
            merge(replace, remove);
        
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }
    
    @Test
    public void testMergeReplaceAndRemoveBeyondUpdate() {
        
        // write "125", replace it "12" with "34", then remove "45", same as replace "125" with "3"
        
        final String replaced = "12";
        final Edit<TextReplace> replace = replace(1, replaced, "34");

        final String removed = "45";
        final Edit<TextRemove> remove = remove(2, removed);
        
        final TextReplace mergeReplace = (TextReplace)merge(replace, remove);
        
        assertThat(mergeReplace.getOldLength()).isEqualTo(3);
        assertThat(mergeReplace.getOldText()).isEqualTo(text("125"));

        assertThat(mergeReplace.getNewLength()).isEqualTo(1);
        assertThat(mergeReplace.getNewText()).isEqualTo(text("3"));
    }

    @Test
    public void testMergeRemoveAndReplaceBeforeStartPosThrowsException() {
        
        final String removed = "123";
        
        final Edit<TextRemove> remove = remove(1, removed);
        
        final String replaced = "3" + removed.substring(0, 1);
        
        final Edit<TextReplace> replace = replace(0, replaced, "345");
        
        try {
            merge(remove, replace);
        
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }
    
    @Test
    public void testMergeRemoveAndReplaceAtSameLength() {
        
        final String removed = "12";
        final Edit<TextRemove> remove = remove(1, removed);

        final String replaced = "34";
        final Edit<TextReplace> replace = replace(1, replaced, "45");
        
        final TextReplace merged = (TextReplace)merge(remove, replace);
        
        assertThat(merged.getOldLength()).isEqualTo(4);
        assertThat(merged.getOldText()).isEqualTo(text("1234"));

        assertThat(merged.getNewLength()).isEqualTo(2);
        assertThat(merged.getNewText()).isEqualTo(text("45"));
    }

    @Test
    public void testMergeRemoveAndReplaceAtDifferingLength() {
        
        final String removed = "12";
        final Edit<TextRemove> remove = remove(1, removed);

        final String replaced = "234";
        final Edit<TextReplace> replace = replace(1, replaced, "345");
        
        final TextReplace merged = (TextReplace)merge(remove, replace);
        
        assertThat(merged.getOldLength()).isEqualTo(5);
        assertThat(merged.getOldText()).isEqualTo(text("12234"));

        assertThat(merged.getNewLength()).isEqualTo(3);
        assertThat(merged.getNewText()).isEqualTo(text("345"));
    }

    @Test
    public void testMergeRemoveAndReplaceAtOtherPosThrowsException() {

        final String removed = "123";
        final Edit<TextRemove> remove = remove(1, removed);
        
        final String replaced = "34";
        final Edit<TextReplace> replace = replace(2, replaced, "456");
        
        try {
            merge(remove, replace);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }
    
    @Test
    public void testMergeRemoveAndRemoveBeforeStartPosThrowsException() {
        
        final String removed1 = "123";
        final Edit<TextRemove> remove1 = remove(1, removed1);
        
        final String removed2 = "3" + removed1.substring(0, 1);
        final Edit<TextRemove> remove2 = remove(0, removed2);
        
        try {
            merge(remove1, remove2);
        
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }
    
    @Test
    public void testMergeRemoveAndRemoveAtSame() {
        
        final String removed1 = "12";
        final Edit<TextRemove> remove1 = remove(1, removed1);

        final String removed2 = "34";
        final Edit<TextRemove> remove2 = remove(1, removed2);
        
        final TextRemove merged = (TextRemove)merge(remove1, remove2);
        
        assertThat(merged.getOldLength()).isEqualTo(4);
        assertThat(merged.getOldText()).isEqualTo(text("1234"));

        assertThat(merged.getNewLength()).isEqualTo(0);
        assertThat(merged.getNewText().isEmpty()).isTrue();
    }

    @Test
    public void testMergeRemoveAndRemoveAtDifferingLength() {
        
        final String removed1 = "12";
        final Edit<TextRemove> remove1 = remove(1, removed1);

        final String removed2 = "345";
        final Edit<TextRemove> remove2 = remove(1, removed2);
        
        final TextRemove merged = (TextRemove)merge(remove1, remove2);
        
        assertThat(merged.getOldLength()).isEqualTo(5);
        assertThat(merged.getOldText()).isEqualTo(text("12345"));

        assertThat(merged.getNewLength()).isEqualTo(0);
        assertThat(merged.getNewText().isEmpty()).isTrue();
    }

    @Test
    public void testMergeRemoveAndRemoveAtOtherPosThrowsException() {

        final String removed1 = "123";
        final Edit<TextRemove> remove1 = remove(1, removed1);
        
        final String removed2 = "34";
        final Edit<TextRemove> remove2 = remove(2, removed2);
        
        try {
            merge(remove1, remove2);

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
        
        final Edit<TextAdd> add = add(startPos1, text1);

        assertThat(toRemoveCount).isEqualTo(toRemove.length());
        
        final Edit<TextRemove> remove = remove(startPos2, toRemove);

        final TextEdit merged = merge(add, remove);

        checkAdd(merged, expectedRemaining, startPos1);
    }

    private void checkMergeAddAndRemoveBeyond(
            String expectedRemoved,
            long startPos1, String text1,
            long startPos2, long toRemoveCount, String toRemove) {
        
        final Edit<TextAdd> add = add(startPos1, text1);

        assertThat(toRemoveCount).isEqualTo(toRemove.length());

        final Edit<TextRemove> remove = remove(startPos2, toRemove);

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
    }

    private void checkMergeAddAndRemoveBeyondReplace(
            String oldText,
            String updated,
            long startPos1, String text1,
            long startPos2, long toRemoveCount, String toRemove) {

        final Edit<TextAdd> add = add(startPos1, text1);
        
        assertThat(toRemoveCount).isEqualTo(toRemove.length());

        final Edit<TextRemove> remove = remove(startPos2, toRemove);

        final TextEdit merged = merge(add, remove);

        checkReplace(merged, oldText, updated);
    }

    private static TextEdit merge(Edit<? extends TextEdit> textEdit1, Edit<? extends TextEdit> textEdit2) {
        
        return TextEditMergeHelper.merge(
                textEdit1.getEdit(), textEdit1.getStartPos(),
                textEdit2.getEdit(), textEdit2.getStartPos());
    }
}
