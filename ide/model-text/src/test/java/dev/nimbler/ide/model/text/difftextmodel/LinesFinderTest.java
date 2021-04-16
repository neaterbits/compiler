package dev.nimbler.ide.model.text.difftextmodel;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

import dev.nimbler.ide.util.ui.text.CharText;
import dev.nimbler.ide.util.ui.text.Text;
import dev.nimbler.ide.util.ui.text.UnixLineDelimiter;

public class LinesFinderTest {

	@Test
	public void testFindLineOffsetsSingleLine() {
		
		final String string = "text";

		final Text text = new CharText(string);
		
		final LinesOffsets offsets = LinesFinder.findLineOffsets(text, UnixLineDelimiter.INSTANCE);

		assertThat(offsets).isNotNull();
		assertThat(offsets.getNumLines()).isEqualTo(1);

		assertThat(offsets.getLineAtOffset(0)).isEqualTo(0);
		assertThat(offsets.getLineAtOffset(1)).isEqualTo(0);
		assertThat(offsets.getLineAtOffset(2)).isEqualTo(0);
		assertThat(offsets.getLineAtOffset(3)).isEqualTo(0);
		
		try {
			offsets.getLineAtOffset(4);
			fail("Expected exeption");
		}
		catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testFindLineOffsetsSingleLineWithEOL() {
		
		final String string = "text\n";

		final Text text = new CharText(string);
		
		final LinesOffsets offsets = LinesFinder.findLineOffsets(text, UnixLineDelimiter.INSTANCE);

		assertThat(offsets).isNotNull();
		assertThat(offsets.getNumLines()).isEqualTo(1);

		assertThat(offsets.getLineAtOffset(0)).isEqualTo(0);
		assertThat(offsets.getLineAtOffset(1)).isEqualTo(0);
		assertThat(offsets.getLineAtOffset(2)).isEqualTo(0);
		assertThat(offsets.getLineAtOffset(3)).isEqualTo(0);
		assertThat(offsets.getLineAtOffset(4)).isEqualTo(0);
		
		try {
			offsets.getLineAtOffset(5);
			fail("Expected exeption");
		}
		catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testFindLineOffsets() {
		
		final String string = "some\nlines\n\nof\ntext";

		final Text text = new CharText(string);
		
		final LinesOffsets offsets = LinesFinder.findLineOffsets(text, UnixLineDelimiter.INSTANCE);
		
		assertThat(offsets).isNotNull();
		assertThat(offsets.getNumLines()).isEqualTo(5);

		try {
			offsets.getLineAtOffset(-1);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		assertThat(offsets.getLineAtOffset(0)).isEqualTo(0);
		assertThat(offsets.getLineAtOffset(1)).isEqualTo(0);
		assertThat(offsets.getLineAtOffset(2)).isEqualTo(0);
		assertThat(offsets.getLineAtOffset(3)).isEqualTo(0);
		assertThat(offsets.getLineAtOffset(4)).isEqualTo(0);
		assertThat(offsets.getLineAtOffset(5)).isEqualTo(1);
		assertThat(offsets.getLineAtOffset(6)).isEqualTo(1);
		assertThat(offsets.getLineAtOffset(7)).isEqualTo(1);
		assertThat(offsets.getLineAtOffset(8)).isEqualTo(1);
		assertThat(offsets.getLineAtOffset(9)).isEqualTo(1);
		assertThat(offsets.getLineAtOffset(10)).isEqualTo(1);
		assertThat(offsets.getLineAtOffset(11)).isEqualTo(2);
		assertThat(offsets.getLineAtOffset(12)).isEqualTo(3);
		assertThat(offsets.getLineAtOffset(13)).isEqualTo(3);
		assertThat(offsets.getLineAtOffset(14)).isEqualTo(3);
		assertThat(offsets.getLineAtOffset(15)).isEqualTo(4);
		assertThat(offsets.getLineAtOffset(16)).isEqualTo(4);
		assertThat(offsets.getLineAtOffset(17)).isEqualTo(4);
		assertThat(offsets.getLineAtOffset(18)).isEqualTo(4);
		
		try {
			offsets.getLineAtOffset(19);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		try {
			offsets.getOffsetForLine(-1);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		assertThat(offsets.getOffsetForLine(0)).isEqualTo(0);
		assertThat(offsets.getOffsetForLine(1)).isEqualTo(5);
		assertThat(offsets.getOffsetForLine(2)).isEqualTo(11);
		assertThat(offsets.getOffsetForLine(3)).isEqualTo(12);
		assertThat(offsets.getOffsetForLine(4)).isEqualTo(15);
		
		try {
			offsets.getOffsetForLine(5);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		try {
			offsets.getLengthOfLineWithAnyNewline(-1);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		assertThat(offsets.getLengthOfLineWithAnyNewline(0)).isEqualTo(5);
		assertThat(offsets.getLengthOfLineWithAnyNewline(1)).isEqualTo(6);
		assertThat(offsets.getLengthOfLineWithAnyNewline(2)).isEqualTo(1);
		assertThat(offsets.getLengthOfLineWithAnyNewline(3)).isEqualTo(3);
		assertThat(offsets.getLengthOfLineWithAnyNewline(4)).isEqualTo(4);
		
		try {
			offsets.getLengthOfLineWithAnyNewline(5);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
	}
}
