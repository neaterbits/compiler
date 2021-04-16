package dev.nimbler.ide.model.text;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

import dev.nimbler.ide.util.ui.text.LineDelimiter;
import dev.nimbler.ide.util.ui.text.StringText;
import dev.nimbler.ide.util.ui.text.UnixLineDelimiter;
import dev.nimbler.ide.util.ui.text.WindowsLineDelimiter;

public class TextNewlinesTest {

	@Test
	public void testPosOfLine() {
		
		final StringText text = new StringText("some\nlines\n\nof\ntext");

		final LineDelimiter lineDelimiter = UnixLineDelimiter.INSTANCE;
		
		try {
			text.findPosOfLineIndex(-1, lineDelimiter);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
		
		assertThat(text.findPosOfLineIndex(0, lineDelimiter)).isEqualTo(0);
		assertThat(text.findPosOfLineIndex(1, lineDelimiter)).isEqualTo(5);
		assertThat(text.findPosOfLineIndex(2, lineDelimiter)).isEqualTo(11);
		assertThat(text.findPosOfLineIndex(3, lineDelimiter)).isEqualTo(12);
		assertThat(text.findPosOfLineIndex(4, lineDelimiter)).isEqualTo(15);
		assertThat(text.findPosOfLineIndex(5, lineDelimiter)).isEqualTo(-1L);
		
	}

	@Test
	public void testLineAtPos() {
		
		final StringText text = new StringText("some\nlines\n\nof\ntext");

		final LineDelimiter lineDelimiter = UnixLineDelimiter.INSTANCE;

		try {
			text.findLineIndexAtPos(-1, lineDelimiter);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		assertThat(text.findLineIndexAtPos(0, lineDelimiter)).isEqualTo(0);
		assertThat(text.findLineIndexAtPos(1, lineDelimiter)).isEqualTo(0);
		assertThat(text.findLineIndexAtPos(2, lineDelimiter)).isEqualTo(0);
		assertThat(text.findLineIndexAtPos(3, lineDelimiter)).isEqualTo(0);
		assertThat(text.findLineIndexAtPos(4, lineDelimiter)).isEqualTo(0);
		assertThat(text.findLineIndexAtPos(5, lineDelimiter)).isEqualTo(1);
		assertThat(text.findLineIndexAtPos(6, lineDelimiter)).isEqualTo(1);
		assertThat(text.findLineIndexAtPos(7, lineDelimiter)).isEqualTo(1);
		assertThat(text.findLineIndexAtPos(8, lineDelimiter)).isEqualTo(1);
		assertThat(text.findLineIndexAtPos(9, lineDelimiter)).isEqualTo(1);
		assertThat(text.findLineIndexAtPos(10, lineDelimiter)).isEqualTo(1);
		assertThat(text.findLineIndexAtPos(11, lineDelimiter)).isEqualTo(2);
		assertThat(text.findLineIndexAtPos(12, lineDelimiter)).isEqualTo(3);
		assertThat(text.findLineIndexAtPos(13, lineDelimiter)).isEqualTo(3);
		assertThat(text.findLineIndexAtPos(14, lineDelimiter)).isEqualTo(3);
		assertThat(text.findLineIndexAtPos(15, lineDelimiter)).isEqualTo(4);
		assertThat(text.findLineIndexAtPos(16, lineDelimiter)).isEqualTo(4);
		assertThat(text.findLineIndexAtPos(17, lineDelimiter)).isEqualTo(4);
		assertThat(text.findLineIndexAtPos(18, lineDelimiter)).isEqualTo(4);
		
		try {
			text.findLineIndexAtPos(19, lineDelimiter);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testLineAtPosEOLAtEOF() {
		
		final StringText text = new StringText("some\nlines\n\nof\ntext\n");

		final LineDelimiter lineDelimiter = UnixLineDelimiter.INSTANCE;

		assertThat(text.findLineIndexAtPos(19, lineDelimiter)).isEqualTo(4);

		try {
			text.findLineIndexAtPos(20, lineDelimiter);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testFindBeginningOfEmptyLine() {
		final StringText text = new StringText("some\n");

		final LineDelimiter lineDelimiter = UnixLineDelimiter.INSTANCE;

		assertThat(text.findBeginningOfNextLine(0, lineDelimiter)).isEqualTo(-1);
		assertThat(text.findBeginningOfNextLine(1, lineDelimiter)).isEqualTo(-1);
		assertThat(text.findBeginningOfNextLine(2, lineDelimiter)).isEqualTo(-1);
		assertThat(text.findBeginningOfNextLine(3, lineDelimiter)).isEqualTo(-1);
		assertThat(text.findBeginningOfNextLine(4, lineDelimiter)).isEqualTo(-1);
		
	}

	
	@Test
	public void testFindBeginningOfNextLine() {
		final StringText text = new StringText("some\nlines\n\nof\ntext");

		final LineDelimiter lineDelimiter = UnixLineDelimiter.INSTANCE;

		try {
			text.findBeginningOfNextLine(-1, lineDelimiter);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		assertThat(text.findBeginningOfNextLine(0, lineDelimiter)).isEqualTo(5);
		assertThat(text.findBeginningOfNextLine(1, lineDelimiter)).isEqualTo(5);
		assertThat(text.findBeginningOfNextLine(2, lineDelimiter)).isEqualTo(5);
		assertThat(text.findBeginningOfNextLine(3, lineDelimiter)).isEqualTo(5);
		assertThat(text.findBeginningOfNextLine(4, lineDelimiter)).isEqualTo(5);
		assertThat(text.findBeginningOfNextLine(5, lineDelimiter)).isEqualTo(11);
		assertThat(text.findBeginningOfNextLine(6, lineDelimiter)).isEqualTo(11);
		assertThat(text.findBeginningOfNextLine(7, lineDelimiter)).isEqualTo(11);
		assertThat(text.findBeginningOfNextLine(8, lineDelimiter)).isEqualTo(11);
		assertThat(text.findBeginningOfNextLine(9, lineDelimiter)).isEqualTo(11);
		assertThat(text.findBeginningOfNextLine(10, lineDelimiter)).isEqualTo(11);
		assertThat(text.findBeginningOfNextLine(11, lineDelimiter)).isEqualTo(12);
		assertThat(text.findBeginningOfNextLine(12, lineDelimiter)).isEqualTo(15);
		assertThat(text.findBeginningOfNextLine(13, lineDelimiter)).isEqualTo(15);
		assertThat(text.findBeginningOfNextLine(14, lineDelimiter)).isEqualTo(15);
		assertThat(text.findBeginningOfNextLine(15, lineDelimiter)).isEqualTo(-1);
		assertThat(text.findBeginningOfNextLine(16, lineDelimiter)).isEqualTo(-1);
		assertThat(text.findBeginningOfNextLine(17, lineDelimiter)).isEqualTo(-1);
		assertThat(text.findBeginningOfNextLine(18, lineDelimiter)).isEqualTo(-1);
		
		try {
			text.findBeginningOfNextLine(19, lineDelimiter);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testFindBeginningOfNextLinePosEOLAtStart() {
		
		final StringText text = new StringText("\nsome\nlines\n\nof\ntext\n");

		final LineDelimiter lineDelimiter = UnixLineDelimiter.INSTANCE;

		assertThat(text.findBeginningOfNextLine(0, lineDelimiter)).isEqualTo(1);
		assertThat(text.findBeginningOfNextLine(1, lineDelimiter)).isEqualTo(6);
		assertThat(text.findBeginningOfNextLine(2, lineDelimiter)).isEqualTo(6);
		assertThat(text.findBeginningOfNextLine(3, lineDelimiter)).isEqualTo(6);
		assertThat(text.findBeginningOfNextLine(4, lineDelimiter)).isEqualTo(6);

		try {
			text.findBeginningOfNextLine(text.length(), lineDelimiter);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testFindBeginningOfNextLinePosEOLAtEOF() {
		
		final StringText text = new StringText("some\nlines\n\nof\ntext\n");

		final LineDelimiter lineDelimiter = UnixLineDelimiter.INSTANCE;

		assertThat(text.findBeginningOfNextLine(15, lineDelimiter)).isEqualTo(-1);
		assertThat(text.findBeginningOfNextLine(16, lineDelimiter)).isEqualTo(-1);
		assertThat(text.findBeginningOfNextLine(17, lineDelimiter)).isEqualTo(-1);
		assertThat(text.findBeginningOfNextLine(18, lineDelimiter)).isEqualTo(-1);
		assertThat(text.findBeginningOfNextLine(19, lineDelimiter)).isEqualTo(-1);

		try {
			text.findBeginningOfNextLine(20, lineDelimiter);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testFindLFNewline() {
		final StringText text = new StringText("some\nlines\n\nof\ntext");

		final LineDelimiter lineDelimiter = UnixLineDelimiter.INSTANCE;

		try {
			text.findBeginningOfNextLine(-1, lineDelimiter);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		assertThat(text.findNewline(0, lineDelimiter)).isEqualTo(4);
		assertThat(text.findNewline(1, lineDelimiter)).isEqualTo(4);
		assertThat(text.findNewline(2, lineDelimiter)).isEqualTo(4);
		assertThat(text.findNewline(3, lineDelimiter)).isEqualTo(4);
		assertThat(text.findNewline(4, lineDelimiter)).isEqualTo(4);
		assertThat(text.findNewline(5, lineDelimiter)).isEqualTo(10);
		assertThat(text.findNewline(6, lineDelimiter)).isEqualTo(10);
		assertThat(text.findNewline(7, lineDelimiter)).isEqualTo(10);
		assertThat(text.findNewline(8, lineDelimiter)).isEqualTo(10);
		assertThat(text.findNewline(9, lineDelimiter)).isEqualTo(10);
		assertThat(text.findNewline(10, lineDelimiter)).isEqualTo(10);
		assertThat(text.findNewline(11, lineDelimiter)).isEqualTo(11);
		assertThat(text.findNewline(12, lineDelimiter)).isEqualTo(14);
		assertThat(text.findNewline(13, lineDelimiter)).isEqualTo(14);
		assertThat(text.findNewline(14, lineDelimiter)).isEqualTo(14);
		assertThat(text.findNewline(15, lineDelimiter)).isEqualTo(-1);
		assertThat(text.findNewline(16, lineDelimiter)).isEqualTo(-1);
		assertThat(text.findNewline(17, lineDelimiter)).isEqualTo(-1);
		assertThat(text.findNewline(18, lineDelimiter)).isEqualTo(-1);
		
		try {
			text.findNewline(19, lineDelimiter);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testFindLFNewlineEOLAtStart() {
		
		final StringText text = new StringText("\nsome\nlines\n\nof\ntext");

		final LineDelimiter lineDelimiter = UnixLineDelimiter.INSTANCE;

		assertThat(text.findNewline(0, lineDelimiter)).isEqualTo(0);
		assertThat(text.findNewline(1, lineDelimiter)).isEqualTo(5);
		assertThat(text.findNewline(2, lineDelimiter)).isEqualTo(5);
		assertThat(text.findNewline(3, lineDelimiter)).isEqualTo(5);
		assertThat(text.findNewline(4, lineDelimiter)).isEqualTo(5);

		try {
			text.findNewline(text.length(), lineDelimiter);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testFindLFNewlinePosEOLAtEOF() {
		
		final StringText text = new StringText("some\nlines\n\nof\ntext\n");

		final LineDelimiter lineDelimiter = UnixLineDelimiter.INSTANCE;

		assertThat(text.findNewline(15, lineDelimiter)).isEqualTo(19);
		assertThat(text.findNewline(16, lineDelimiter)).isEqualTo(19);
		assertThat(text.findNewline(17, lineDelimiter)).isEqualTo(19);
		assertThat(text.findNewline(18, lineDelimiter)).isEqualTo(19);
		assertThat(text.findNewline(19, lineDelimiter)).isEqualTo(19);

		try {
			text.findLineIndexAtPos(20, lineDelimiter);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testFindCRLFNewline() {
		final StringText text = new StringText("some\r\nlines\r\n\r\nof\r\ntext");

		final LineDelimiter lineDelimiter = WindowsLineDelimiter.INSTANCE;

		try {
			text.findBeginningOfNextLine(-1, lineDelimiter);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		assertThat(text.findNewline(0, lineDelimiter)).isEqualTo(4);
		assertThat(text.findNewline(1, lineDelimiter)).isEqualTo(4);
		assertThat(text.findNewline(2, lineDelimiter)).isEqualTo(4);
		assertThat(text.findNewline(3, lineDelimiter)).isEqualTo(4);
		assertThat(text.findNewline(4, lineDelimiter)).isEqualTo(4);
		assertThat(text.findNewline(5, lineDelimiter)).isEqualTo(11);
		assertThat(text.findNewline(6, lineDelimiter)).isEqualTo(11);
		assertThat(text.findNewline(7, lineDelimiter)).isEqualTo(11);
		assertThat(text.findNewline(8, lineDelimiter)).isEqualTo(11);
		assertThat(text.findNewline(9, lineDelimiter)).isEqualTo(11);
		assertThat(text.findNewline(10, lineDelimiter)).isEqualTo(11);
		assertThat(text.findNewline(11, lineDelimiter)).isEqualTo(11);
		assertThat(text.findNewline(12, lineDelimiter)).isEqualTo(13);
		assertThat(text.findNewline(13, lineDelimiter)).isEqualTo(13);
		assertThat(text.findNewline(14, lineDelimiter)).isEqualTo(17);
		assertThat(text.findNewline(15, lineDelimiter)).isEqualTo(17);
		assertThat(text.findNewline(16, lineDelimiter)).isEqualTo(17);
		assertThat(text.findNewline(17, lineDelimiter)).isEqualTo(17);
		assertThat(text.findNewline(18, lineDelimiter)).isEqualTo(-1);
		assertThat(text.findNewline(19, lineDelimiter)).isEqualTo(-1);
		assertThat(text.findNewline(20, lineDelimiter)).isEqualTo(-1);
		assertThat(text.findNewline(21, lineDelimiter)).isEqualTo(-1);
		assertThat(text.findNewline(22, lineDelimiter)).isEqualTo(-1);
		
		try {
			text.findNewline(23, lineDelimiter);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testFindCRLFNewlineEOLAtStart() {
		
		final StringText text = new StringText("\r\nsome\r\nlines\r\n\r\nof\r\ntext");

		final LineDelimiter lineDelimiter = WindowsLineDelimiter.INSTANCE;

		assertThat(text.findNewline(0, lineDelimiter)).isEqualTo(0);
		assertThat(text.findNewline(1, lineDelimiter)).isEqualTo(6);
		assertThat(text.findNewline(2, lineDelimiter)).isEqualTo(6);
		assertThat(text.findNewline(3, lineDelimiter)).isEqualTo(6);
		assertThat(text.findNewline(4, lineDelimiter)).isEqualTo(6);

		try {
			text.findNewline(text.length(), lineDelimiter);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testfindCRLFNewlineEOLAtEOF() {
		
		final StringText text = new StringText("some\r\nlines\r\n\r\nof\r\ntext\r\n");

		assertThat(text.length()).isEqualTo(25);
		
		final LineDelimiter lineDelimiter = WindowsLineDelimiter.INSTANCE;
		
 		assertThat(text.findNewline(17, lineDelimiter)).isEqualTo(17);
 		assertThat(text.findNewline(18, lineDelimiter)).isEqualTo(23);
		assertThat(text.findNewline(19, lineDelimiter)).isEqualTo(23);
		assertThat(text.findNewline(20, lineDelimiter)).isEqualTo(23);
		assertThat(text.findNewline(21, lineDelimiter)).isEqualTo(23);
		assertThat(text.findNewline(22, lineDelimiter)).isEqualTo(23);
		assertThat(text.findNewline(23, lineDelimiter)).isEqualTo(23);
		assertThat(text.findNewline(24, lineDelimiter)).isEqualTo(-1L);

		try {
			text.findLineIndexAtPos(25
					, lineDelimiter);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
	}

	@Test
	public void testGetNumberOfLFNewlineChars() {

		final StringText text = new StringText("some\nlines\n\nof\ntext");

		final LineDelimiter lineDelimiter = UnixLineDelimiter.INSTANCE;

		assertThat(text.getNumberOfNewlineChars(lineDelimiter)).isEqualTo(4);
	}

	@Test
	public void testGetNumberOfLFNewlineCharsEOLAtEOF() {

		final StringText text = new StringText("some\nlines\n\nof\ntext\n");

		final LineDelimiter lineDelimiter = UnixLineDelimiter.INSTANCE;

		assertThat(text.getNumberOfNewlineChars(lineDelimiter)).isEqualTo(5);
	}

	@Test
	public void testGetNumberOfLFNewlineCharsOneCharAtEOF() {

		final StringText text = new StringText("some\nlines\n\nof\ntext\nx");

		final LineDelimiter lineDelimiter = UnixLineDelimiter.INSTANCE;

		assertThat(text.getNumberOfNewlineChars(lineDelimiter)).isEqualTo(5);
	}

	@Test
	public void testGetNumberOfCRLFNewlineChars() {

		final StringText text = new StringText("some\r\nlines\r\n\r\nof\r\ntext");

		final LineDelimiter lineDelimiter = WindowsLineDelimiter.INSTANCE;

		assertThat(text.getNumberOfNewlineChars(lineDelimiter)).isEqualTo(4);
	}

	@Test
	public void testGetNumberOfCRLFNewlineCharsEOLAtEOF() {

		final StringText text = new StringText("some\r\nlines\r\n\r\nof\r\ntext\r\n");

		final LineDelimiter lineDelimiter = WindowsLineDelimiter.INSTANCE;

		assertThat(text.getNumberOfNewlineChars(lineDelimiter)).isEqualTo(5);
	}

	@Test
	public void testGetNumberOfCRLFNewlineCharsOneCharAtEOF() {

		final StringText text = new StringText("some\r\nlines\r\n\r\nof\r\ntext\r\nx");

		final LineDelimiter lineDelimiter = WindowsLineDelimiter.INSTANCE;

		assertThat(text.getNumberOfNewlineChars(lineDelimiter)).isEqualTo(5);
	}

	@Test
	public void testGetNumberOfLFLines() {

		final StringText text = new StringText("some\nlines\n\nof\ntext");

		final LineDelimiter lineDelimiter = UnixLineDelimiter.INSTANCE;

		assertThat(text.getNumberOfLinesIncludingTrailingNonTerminated(lineDelimiter)).isEqualTo(5);
	}

	@Test
	public void testGetNumberOfLFLinesEOLAtEOF() {

		final StringText text = new StringText("some\nlines\n\nof\ntext\n");

		final LineDelimiter lineDelimiter = UnixLineDelimiter.INSTANCE;

		assertThat(text.getNumberOfLinesIncludingTrailingNonTerminated(lineDelimiter)).isEqualTo(5);
	}

	@Test
	public void testGetNumberOfLFLinesOneCharAtEOF() {

		final StringText text = new StringText("some\nlines\n\nof\ntext\nx");

		final LineDelimiter lineDelimiter = UnixLineDelimiter.INSTANCE;

		assertThat(text.getNumberOfLinesIncludingTrailingNonTerminated(lineDelimiter)).isEqualTo(6);
	}

	@Test
	public void testGetNumberOfCRLFLines() {

		final StringText text = new StringText("some\r\nlines\r\n\r\nof\r\ntext");

		final LineDelimiter lineDelimiter = WindowsLineDelimiter.INSTANCE;

		assertThat(text.getNumberOfLinesIncludingTrailingNonTerminated(lineDelimiter)).isEqualTo(5);
	}

	@Test
	public void testGetNumberOfCRLFLinesEOLAtEOF() {

		final StringText text = new StringText("some\r\nlines\r\n\r\nof\r\ntext\r\n");

		final LineDelimiter lineDelimiter = WindowsLineDelimiter.INSTANCE;

		assertThat(text.getNumberOfLinesIncludingTrailingNonTerminated(lineDelimiter)).isEqualTo(5);
	}

	@Test
	public void testGetNumberOfCRLFLinesOneCharAtEOF() {

		final StringText text = new StringText("some\r\nlines\r\n\r\nof\r\ntext\r\nx");

		final LineDelimiter lineDelimiter = WindowsLineDelimiter.INSTANCE;

		assertThat(text.getNumberOfLinesIncludingTrailingNonTerminated(lineDelimiter)).isEqualTo(6);
	}
}
