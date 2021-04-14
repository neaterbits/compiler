package dev.nimbler.ide.model.text;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

import dev.nimbler.ide.util.ui.text.LineDelimiter;
import dev.nimbler.ide.util.ui.text.StringText;
import dev.nimbler.ide.util.ui.text.Text;

public class UnixLineDelimiterTest {

	@Test
	public void testNewlines() {
		
		final Text text = new StringText("\nabcd\nmore\n\ntext");

		assertThat(text.length()).isEqualTo(16);
		
		final LineDelimiter lineDelimiter = UnixLineDelimiter.INSTANCE;
		
		assertThat(lineDelimiter.getNumberOfNewlineCharsForOneLineShift(text, 0)).isEqualTo(1);
		assertThat(lineDelimiter.getNumberOfNewlineCharsForOneLineShift(text, 1)).isEqualTo(0);
		assertThat(lineDelimiter.getNumberOfNewlineCharsForOneLineShift(text, 2)).isEqualTo(0);
		assertThat(lineDelimiter.getNumberOfNewlineCharsForOneLineShift(text, 3)).isEqualTo(0);
		assertThat(lineDelimiter.getNumberOfNewlineCharsForOneLineShift(text, 4)).isEqualTo(0);
		assertThat(lineDelimiter.getNumberOfNewlineCharsForOneLineShift(text, 5)).isEqualTo(1);
		assertThat(lineDelimiter.getNumberOfNewlineCharsForOneLineShift(text, 6)).isEqualTo(0);
		assertThat(lineDelimiter.getNumberOfNewlineCharsForOneLineShift(text, 7)).isEqualTo(0);
		assertThat(lineDelimiter.getNumberOfNewlineCharsForOneLineShift(text, 8)).isEqualTo(0);
		assertThat(lineDelimiter.getNumberOfNewlineCharsForOneLineShift(text, 9)).isEqualTo(0);
		assertThat(lineDelimiter.getNumberOfNewlineCharsForOneLineShift(text, 10)).isEqualTo(1);
		assertThat(lineDelimiter.getNumberOfNewlineCharsForOneLineShift(text, 11)).isEqualTo(1);
		assertThat(lineDelimiter.getNumberOfNewlineCharsForOneLineShift(text, 12)).isEqualTo(0);
		assertThat(lineDelimiter.getNumberOfNewlineCharsForOneLineShift(text, 13)).isEqualTo(0);
		assertThat(lineDelimiter.getNumberOfNewlineCharsForOneLineShift(text, 14)).isEqualTo(0);
		assertThat(lineDelimiter.getNumberOfNewlineCharsForOneLineShift(text, 15)).isEqualTo(0);
	}

	@Test
	public void testNewlinesEOLAtEOF() {

		final Text text = new StringText("text\n");
		
		assertThat(text.length()).isEqualTo(5);
		
		final LineDelimiter lineDelimiter = UnixLineDelimiter.INSTANCE;
		
		assertThat(lineDelimiter.getNumberOfNewlineCharsForOneLineShift(text, 0)).isEqualTo(0);
		assertThat(lineDelimiter.getNumberOfNewlineCharsForOneLineShift(text, 1)).isEqualTo(0);
		assertThat(lineDelimiter.getNumberOfNewlineCharsForOneLineShift(text, 2)).isEqualTo(0);
		assertThat(lineDelimiter.getNumberOfNewlineCharsForOneLineShift(text, 3)).isEqualTo(0);
		assertThat(lineDelimiter.getNumberOfNewlineCharsForOneLineShift(text, 4)).isEqualTo(1);
	}

	@Test
	public void testNegativeOffset() {

		final Text text = new StringText("\nabcd\nmore\n\ntext");
		
		assertThat(text.length()).isEqualTo(16);
		
		final LineDelimiter lineDelimiter = UnixLineDelimiter.INSTANCE;

		try {
			lineDelimiter.getNumberOfNewlineCharsForOneLineShift(text, -1L);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testLargerOffset() {

		final Text text = new StringText("\nabcd\nmore\n\ntext");
		
		assertThat(text.length()).isEqualTo(16);
		
		final LineDelimiter lineDelimiter = UnixLineDelimiter.INSTANCE;

		try {
			lineDelimiter.getNumberOfNewlineCharsForOneLineShift(text, text.length());
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
	}
}
