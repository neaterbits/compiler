package dev.nimbler.ide.model.text.difftextmodel;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import dev.nimbler.ide.model.text.BaseTextEditTest;
import dev.nimbler.ide.model.text.TextAdd;
import dev.nimbler.ide.model.text.TextReplace;
import dev.nimbler.ide.util.ui.text.StringText;
import dev.nimbler.ide.util.ui.text.Text;
import dev.nimbler.ide.util.ui.text.UnixLineDelimiter;

public class DiffTextOffsetsTest extends BaseTextEditTest {

    @Test
    public void testApplyAddEdit() {
        
        final String initialText = "some\nlines\n\nof text";
        
        final LinesOffsets linesOffsets = LinesFinder.findLineOffsets(
                new StringText(initialText),
                UnixLineDelimiter.INSTANCE);
        
        final DiffTextOffsets offsets = new DiffTextOffsets(Collections.emptyList(), linesOffsets);
        
        // ---------------------- add ----------------------
        final String addText = " more";

        final Edit<TextAdd> add = add(4, addText);
        
        offsets.applyTextEditWithMerge(add.toPosEdit(), linesOffsets);
        
        assertThat(offsets.getOffsetArray().length()).isEqualTo(1);
        assertThat(offsets.getOffsetArray().get(0).getEdit()).isSameAs(add.getEdit());
        assertThat(offsets.getOffsetArray().get(0).getOriginalStartPos()).isEqualTo(4L);
        assertThat(offsets.getOffsetArray().get(0).isLastTextEdit()).isTrue();
        
        final String expectedTextAfterAdd = "some more\nlines\n\nof text";
        final long curTextLengthAfterAdd = expectedTextAfterAdd.length();
        final Text updatedTextAfterAdd = offsets.getText(curTextLengthAfterAdd, text(initialText), linesOffsets);
        
        assertThat(updatedTextAfterAdd).isEqualTo(text(expectedTextAfterAdd));
        assertThat(offsets.getCharCount(curTextLengthAfterAdd, linesOffsets)).isEqualTo(curTextLengthAfterAdd);

        System.out.println("## offsets after add " + offsets);

        // ---------------------- replace ----------------------
        final Edit<TextReplace> replace = replace(5, "more", "yet a few");
        offsets.applyTextEditWithMerge(replace.toPosEdit(), linesOffsets);

        assertThat(offsets.getOffsetArray().length()).isEqualTo(1);
        assertThat(offsets.getOffsetArray().get(0).getEdit())
                .isEqualTo(add(4, " yet a few").getEdit());
        assertThat(offsets.getOffsetArray().get(0).getOriginalStartPos()).isEqualTo(4L);
        assertThat(offsets.getOffsetArray().get(0).isLastTextEdit()).isTrue();
        
        final String expectedTextAfterReplace = "some yet a few\nlines\n\nof text";
        final long curTextLengthAfterReplace = expectedTextAfterReplace.length();

        System.out.println("## offsets after replace " + offsets);
        
        final Text updatedTextAfterReplace = offsets.getText(
                curTextLengthAfterReplace,
                text(initialText),
                linesOffsets);

        assertThat(updatedTextAfterReplace).isEqualTo(text(expectedTextAfterReplace));
        assertThat(offsets.getCharCount(curTextLengthAfterReplace, linesOffsets)).isEqualTo(curTextLengthAfterReplace);

        // ---------------------- add before ----------------------

        final Edit<TextReplace> anotherReplace = replace(1, "om", "uch ar");
        offsets.applyTextEditWithMerge(anotherReplace.toPosEdit(), linesOffsets);

        System.out.println("## offsets after another add " + offsets);
        
        assertThat(offsets.getOffsetArray().length()).isEqualTo(2);

        assertThat(offsets.getOffsetArray().get(0).getEdit())
            .isSameAs(anotherReplace.getEdit());
        assertThat(offsets.getOffsetArray().get(0).getOriginalStartPos()).isEqualTo(1L);
        assertThat(offsets.getOffsetArray().get(0).isLastTextEdit()).isFalse();
        assertThat(offsets.getOffsetArray().get(0).getDistanceToNextTextEdit()).isEqualTo(1);

        assertThat(offsets.getOffsetArray().get(1).getEdit())
                .isEqualTo(add(4, " yet a few").getEdit());
        assertThat(offsets.getOffsetArray().get(1).getOriginalStartPos()).isEqualTo(4L);
        assertThat(offsets.getOffsetArray().get(1).isLastTextEdit()).isTrue();
        
        final String expectedTextAfterAnotherAdd = "such are yet a few\nlines\n\nof text";
        final long curTextLengthAfterAnotherAdd = expectedTextAfterAnotherAdd.length();

        System.out.println("## offset after replace " + offsets);
        
        final Text updatedTextAfterAnotherAdd = offsets.getText(
                curTextLengthAfterAnotherAdd,
                text(initialText),
                linesOffsets);

        assertThat(updatedTextAfterAnotherAdd).isEqualTo(text(expectedTextAfterReplace));
        assertThat(offsets.getCharCount(curTextLengthAfterAnotherAdd, linesOffsets)).isEqualTo(curTextLengthAfterAnotherAdd);
    }
}
