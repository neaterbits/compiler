package dev.nimbler.ide.model.text;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

public class TextEditMergeHelperIsMergeableTest extends BaseTextEditMergeHelperTest {

    @Test
    public void testMergeableWithNegativeStartPosThrowsException() {
        
        try {
            TextEditMergeHelper.isMergeable(
                    new TextAdd(text("12")),
                    -1L,
                    new TextAdd(text("12")),
                    0L);
            
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }

        try {
            TextEditMergeHelper.isMergeable(
                    new TextAdd(text("12")),
                    0L,
                    new TextAdd(text("12")),
                    -1L);
            
            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
            
        }
    }

    @Test
    public void testAddIsMergeableWithAdd() {
        
        assertThat(mergeable(add(0, "12"), add(0, "34"))).isTrue();
        assertThat(mergeable(add(0, "12"), add(1, "34"))).isTrue();
        assertThat(mergeable(add(0, "12"), add(2, "34"))).isTrue();
        assertThat(mergeable(add(0, "12"), add(3, "34"))).isFalse();
        
    }

    @Test
    public void testAddIsMergeableWithReplace() {
        
        assertThat(mergeable(add(0, "12"), replace(0, "12", "34"))).isTrue();
        assertThat(mergeable(add(0, "12"), replace(1, "1", "34"))).isTrue();
        assertThat(mergeable(add(0, "12"), replace(2, "xyz", "34"))).isFalse();
    }

    @Test
    public void testAddIsMergeableWithRemove() {
        
        assertThat(mergeable(add(0, "12"), remove(0, "12"))).isTrue();
        assertThat(mergeable(add(0, "12"), remove(0, "123"))).isTrue();
        assertThat(mergeable(add(0, "12"), remove(1, "1"))).isTrue();
        assertThat(mergeable(add(0, "12"), remove(1, "12"))).isTrue();
        assertThat(mergeable(add(0, "12"), remove(2, "xyz"))).isFalse();
    }

    @Test
    public void testReplaceIsMergeableWithAdd() {
        
        assertThat(mergeable(replace(0, "12", "45"), add(0, "34"))).isTrue();
        assertThat(mergeable(replace(0, "12", "45"), add(1, "34"))).isTrue();
        assertThat(mergeable(replace(0, "12", "45"), add(2, "34"))).isTrue();
        assertThat(mergeable(replace(0, "12", "45"), add(3, "34"))).isFalse();
        
    }

    @Test
    public void testReplaceIsMergeableWithReplace() {
        
        assertThat(mergeable(replace(0, "12", "45"), replace(0, "45", "34"))).isTrue();
        assertThat(mergeable(replace(0, "12", "45"), replace(1, "5", "34"))).isTrue();
        assertThat(mergeable(replace(0, "12", "45"), replace(2, "xyz", "34"))).isFalse();
    }

    @Test
    public void testReplaceIsMergeableWithRemove() {
        
        assertThat(mergeable(replace(0, "12", "45"), remove(0, "12"))).isTrue();
        assertThat(mergeable(replace(0, "12", "45"), remove(0, "123"))).isTrue();
        assertThat(mergeable(replace(0, "12", "45"), remove(1, "1"))).isTrue();
        assertThat(mergeable(replace(0, "12", "45"), remove(1, "12"))).isTrue();
        assertThat(mergeable(replace(0, "12", "45"), remove(2, "xyz"))).isFalse();
    }

    @Test
    public void testRemoveIsMergeableWithAdd() {
        
        assertThat(mergeable(remove(0, "12"), add(0, "34"))).isTrue();
        assertThat(mergeable(remove(0, "12"), add(1, "34"))).isFalse();
        
    }

    @Test
    public void testRemoveIsMergeableWithReplace() {
        
        assertThat(mergeable(remove(0, "12"), replace(0, "12", "34"))).isTrue();
        assertThat(mergeable(remove(0, "12"), replace(1, "1", "34"))).isFalse();
    }

    @Test
    public void testRemoveIsMergeableWithRemove() {
        
        assertThat(mergeable(remove(0, "12"), remove(0, "12"))).isTrue();
        assertThat(mergeable(remove(0, "12"), remove(0, "123"))).isTrue();
        assertThat(mergeable(remove(0, "12"), remove(1, "1"))).isFalse();
    }
    
    private static boolean mergeable(Edit<? extends TextEdit> edit1, Edit<? extends TextEdit> edit2) {

        return TextEditMergeHelper.isMergeable(
                edit1.getEdit(),
                edit1.getStartPos(),
                edit2.getEdit(),
                edit2.getStartPos());
    }
    
}
