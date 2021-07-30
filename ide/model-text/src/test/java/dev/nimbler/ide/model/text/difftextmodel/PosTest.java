package dev.nimbler.ide.model.text.difftextmodel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

public class PosTest {

    @Test
    public void testPosBefore() {
        
        assertThat(Pos.getPos(1L, 3L, 0L)).isEqualTo(Pos.BEFORE);
        assertThat(Pos.getPos(3L, 1L, 2L)).isEqualTo(Pos.BEFORE);
    }

    @Test
    public void testPosAtStartOrAtEnd() {
        
        assertThat(Pos.getPos(0L, 1L, 0L)).isEqualTo(Pos.AT_START_AT_END);
        assertThat(Pos.getPos(3L, 1L, 3L)).isEqualTo(Pos.AT_START_AT_END);
    }

    @Test
    public void testPosAtStart() {
        
        assertThat(Pos.getPos(0L, 4L, 0L)).isEqualTo(Pos.AT_START);

        assertThat(Pos.getPos(1L, 3L, 1L)).isEqualTo(Pos.AT_START);
    }

    @Test
    public void testPosWithin() {
        
        assertThat(Pos.getPos(0L, 4L, 1L)).isEqualTo(Pos.WITHIN);
        assertThat(Pos.getPos(0L, 4L, 2L)).isEqualTo(Pos.WITHIN);
    }

    @Test
    public void testPosAtEnd() {
        
        assertThat(Pos.getPos(0L, 4L, 3L)).isEqualTo(Pos.AT_END);
        assertThat(Pos.getPos(1L, 4L, 4L)).isEqualTo(Pos.AT_END);
    }

    @Test
    public void testPosAfter() {
        
        assertThat(Pos.getPos(0L, 4L, 4L)).isEqualTo(Pos.AFTER);
        assertThat(Pos.getPos(0L, 4L, 5L)).isEqualTo(Pos.AFTER);
    }

    @Test
    public void testPosNoLength() {
        
        try {
            Pos.getPos(1L, 0L, 3L);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testPosNegativeLength() {
        
        try {
            Pos.getPos(1L, -1L, 3L);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testPosNegativeBlockStart() {
        
        try {
            Pos.getPos(-1L, 1L, 3L);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
        }
    }

    @Test
    public void testPosNegativePos() {
        
        try {
            Pos.getPos(0L, 1L, -1L);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {
        }
    }
}
