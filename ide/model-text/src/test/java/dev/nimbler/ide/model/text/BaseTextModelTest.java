package dev.nimbler.ide.model.text;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

import dev.nimbler.ide.util.ui.text.LineDelimiter;
import dev.nimbler.ide.util.ui.text.StringText;
import dev.nimbler.ide.util.ui.text.UnixLineDelimiter;

public abstract class BaseTextModelTest {

	protected abstract TextModel makeTextModel(LineDelimiter lineDelimiter, StringText initialText);
	
	private static final String TEST_STRING = "some\nlines\n\nof\ntext";
	@Test
	public void testInitial() {

		final String string = TEST_STRING;
		
		final StringText initialText = new StringText(string);
		
		final TextModel model = makeTextModel(UnixLineDelimiter.INSTANCE, initialText);
	
		checkModel(model, string);
	}

	@Test
	public void testReplaceWordWithoutNewline() {

		final String replaceText = "lines";
		
		final int offset = TEST_STRING.indexOf(replaceText);
		final String string = TEST_STRING.replace(replaceText, "");
		
		final StringText initialText = new StringText(string);
		
		final TextModel model = makeTextModel(UnixLineDelimiter.INSTANCE, initialText);
		
		model.replaceTextRange(offset, 0, new StringText(replaceText));
	
		checkModel(model, TEST_STRING);
	}

	@Test
	public void testReplacePartOfInitialWordWithoutNewline() {

		final String replaceText = "so";
		
		final int offset = TEST_STRING.indexOf(replaceText);
		final String string = TEST_STRING.replace(replaceText, "");
		
		final StringText initialText = new StringText(string);
		
		final TextModel model = makeTextModel(UnixLineDelimiter.INSTANCE, initialText);
		
		model.replaceTextRange(offset, 0, new StringText(replaceText));
	
		checkModel(model, TEST_STRING);
	}

	@Test
	public void testReplacePartOfWordWithoutNewline() {

		final String replaceText = "some";
		
		final int offset = TEST_STRING.indexOf(replaceText);
		final String string = TEST_STRING.replace(replaceText, "");
		
		final StringText initialText = new StringText(string);
		
		final TextModel model = makeTextModel(UnixLineDelimiter.INSTANCE, initialText);
		
		model.replaceTextRange(offset, 0, new StringText(replaceText));
	
		checkModel(model, TEST_STRING);
	}

	@Test
	public void testReplacePartOfWordWithNewline() {

		final String replaceText = "some\n";
		
		final int offset = TEST_STRING.indexOf(replaceText);
		final String string = TEST_STRING.replace(replaceText, "");
		
		final StringText initialText = new StringText(string);
		
		final TextModel model = makeTextModel(UnixLineDelimiter.INSTANCE, initialText);
		
		model.replaceTextRange(offset, 0, new StringText(replaceText));
	
		checkModel(model, TEST_STRING);
	}

	@Test
	public void testReplaceToRemovePartOfWord() {

		final String replaceText = "some";
		
		final int offset = TEST_STRING.indexOf(replaceText);
		final String string = TEST_STRING.replace(replaceText, "somet");
		
		final StringText initialText = new StringText(string);
		
		final TextModel model = makeTextModel(UnixLineDelimiter.INSTANCE, initialText);
		
		model.replaceTextRange(offset + 4, 1, new StringText(""));
	
		checkModel(model, TEST_STRING);
	}

	private void checkModel(TextModel model, String string) {

		assertThat(model.getText().asString()).isEqualTo(string);

		assertThat(model.getTextRange(0, string.length()).asString()).isEqualTo(string);

		try {
			model.getTextRange(-1, 1);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		try {
			model.getTextRange(0, -1);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		try {
			model.getTextRange(0, string.length() + 1);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		assertThat(model.getTextRange(0, 2).asString()).isEqualTo("so");
		assertThat(model.getTextRange(3, 5).asString()).isEqualTo("e\nlin");
		assertThat(model.getTextRange(3, 0).asString()).isEqualTo("");
		assertThat(model.getTextRange(17, 2).asString()).isEqualTo("xt");

		assertThat(model.getLineDelimiter()).isEqualTo(UnixLineDelimiter.INSTANCE);

		assertThat(model.getCharCount()).isEqualTo(string.length());
		
		assertThat(model.getLineWithoutAnyNewline(0).asString()).isEqualTo("some");
		assertThat(model.getLineWithoutAnyNewline(1).asString()).isEqualTo("lines");
		assertThat(model.getLineWithoutAnyNewline(2).asString()).isEqualTo("");
		assertThat(model.getLineWithoutAnyNewline(3).asString()).isEqualTo("of");
		assertThat(model.getLineWithoutAnyNewline(4).asString()).isEqualTo("text");
		assertThat(model.getLineWithoutAnyNewline(5)).isNull();;

		assertThat(model.getLineIncludingAnyNewline(0).asString()).isEqualTo("some\n");
		assertThat(model.getLineIncludingAnyNewline(1).asString()).isEqualTo("lines\n");
		assertThat(model.getLineIncludingAnyNewline(2).asString()).isEqualTo("\n");
		assertThat(model.getLineIncludingAnyNewline(3).asString()).isEqualTo("of\n");
		assertThat(model.getLineIncludingAnyNewline(4).asString()).isEqualTo("text");
		assertThat(model.getLineIncludingAnyNewline(5)).isNull();;

		assertThat(model.getLineCount()).isEqualTo(5);

		assertThat(model.getLineAtOffset(0)).isEqualTo(0);
		assertThat(model.getLineAtOffset(1)).isEqualTo(0);
		assertThat(model.getLineAtOffset(2)).isEqualTo(0);
		assertThat(model.getLineAtOffset(3)).isEqualTo(0);
		assertThat(model.getLineAtOffset(4)).isEqualTo(0);
		assertThat(model.getLineAtOffset(5)).isEqualTo(1);
		assertThat(model.getLineAtOffset(6)).isEqualTo(1);
		assertThat(model.getLineAtOffset(7)).isEqualTo(1);
		assertThat(model.getLineAtOffset(8)).isEqualTo(1);
		assertThat(model.getLineAtOffset(9)).isEqualTo(1);
		assertThat(model.getLineAtOffset(10)).isEqualTo(1);
		assertThat(model.getLineAtOffset(11)).isEqualTo(2);
		assertThat(model.getLineAtOffset(12)).isEqualTo(3);
		assertThat(model.getLineAtOffset(13)).isEqualTo(3);
		assertThat(model.getLineAtOffset(14)).isEqualTo(3);
		assertThat(model.getLineAtOffset(15)).isEqualTo(4);
		assertThat(model.getLineAtOffset(16)).isEqualTo(4);
		assertThat(model.getLineAtOffset(17)).isEqualTo(4);
		assertThat(model.getLineAtOffset(18)).isEqualTo(4);
		
		try {
			model.getLineAtOffset(19);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		try {
			model.getOffsetAtLine(-1);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
		
		assertThat(model.getOffsetAtLine(0)).isEqualTo(0);
		assertThat(model.getOffsetAtLine(1)).isEqualTo(5);
		assertThat(model.getOffsetAtLine(2)).isEqualTo(11);
		assertThat(model.getOffsetAtLine(3)).isEqualTo(12);
		assertThat(model.getOffsetAtLine(4)).isEqualTo(15);
		assertThat(model.getOffsetAtLine(5)).isEqualTo(-1);
	}
}
