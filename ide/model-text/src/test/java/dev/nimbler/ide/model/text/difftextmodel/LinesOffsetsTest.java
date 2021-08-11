package dev.nimbler.ide.model.text.difftextmodel;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import dev.nimbler.ide.util.ui.text.LongArray;

public class LinesOffsetsTest {

    private static class Counter {
        
        long curOffset;
        long curLine;
        
        void addLine(int length, LongArray offsets, LongArray lengths) {
            
            offsets.set(curLine, curOffset);
            lengths.set(curLine, length);
            
            curOffset += length;
            ++ curLine;
        }
        
        long getTextLength() {
            return curOffset;
        }
        
        long getNumLines() {
            return curLine;
        }
    }
    
    @Test
    public void testLinesOffsets() {
        
        final LongArray offsets = new LongArray(1000, 1000);
        final LongArray lengths = new LongArray(1000, 1000);
        
        final Counter counter = new Counter();

        counter.addLine(3, offsets, lengths);
        counter.addLine(11, offsets, lengths);
        counter.addLine(8, offsets, lengths);
        counter.addLine(16, offsets, lengths);
        counter.addLine(21, offsets, lengths);

        final LinesOffsets linesOffsets = new LinesOffsets(
                5,
                counter.getTextLength(),
                offsets,
                lengths);

        assertThat(linesOffsets.getNumLines()).isEqualTo(counter.getNumLines());
        assertThat(linesOffsets.getTextLength()).isEqualTo(counter.getTextLength());
        
        assertThat(linesOffsets.getLengthOfLineWithAnyNewline(0)).isEqualTo(3);
        assertThat(linesOffsets.getLengthOfLineWithAnyNewline(1)).isEqualTo(11);
        assertThat(linesOffsets.getLengthOfLineWithAnyNewline(2)).isEqualTo(8);
        assertThat(linesOffsets.getLengthOfLineWithAnyNewline(3)).isEqualTo(16);
        assertThat(linesOffsets.getLengthOfLineWithAnyNewline(4)).isEqualTo(21);

        assertThat(linesOffsets.getOffsetForLine(0)).isEqualTo(0);
        assertThat(linesOffsets.getOffsetForLine(1)).isEqualTo(3);
        assertThat(linesOffsets.getOffsetForLine(2)).isEqualTo(3 + 11);
        assertThat(linesOffsets.getOffsetForLine(3)).isEqualTo(3 + 11 + 8);
        assertThat(linesOffsets.getOffsetForLine(4)).isEqualTo(3 + 11 + 8 + 16);

        for (int i = 0; i < 3; ++ i) {
            assertThat(linesOffsets.getLineAtOffset(i)).isEqualTo(0);
        }

        for (int i = 3; i < 3 + 11; ++ i) {
            assertThat(linesOffsets.getLineAtOffset(i)).isEqualTo(1);
        }

        for (int i = 3 + 11; i < 3 + 11 + 8; ++ i) {
            assertThat(linesOffsets.getLineAtOffset(i)).isEqualTo(2);
        }

        for (int i = 3 + 11 + 8; i < 3 + 11 + 8 + 16; ++ i) {
            assertThat(linesOffsets.getLineAtOffset(i)).isEqualTo(3);
        }

        for (int i = 3 + 11 + 8 + 16; i < 3 + 11 + 8 + 16 + 21; ++ i) {
            assertThat(linesOffsets.getLineAtOffset(i)).isEqualTo(4);
        }
    }
}
