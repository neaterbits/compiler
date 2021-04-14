package com.neaterbits.ide.model.text;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

import com.neaterbits.ide.util.ui.text.Text;
import com.neaterbits.ide.util.ui.text.TextRange;

public abstract class BaseTextTest {

	protected abstract Text createText(String string);
	
	@Test
	public void testSubstringOneParamArgs() {
		
		final String string = "some\n lines\n \nof text";
		
		final Text text = createText(string);

		try {
			text.substring(-1);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		try {
			text.substring(string.length() + 1);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
	}
	
	@Test
	public void testSubstringOneParam() {

		final String string = "some\n lines\n \nof text";
		
		final Text text = createText(string);
		
		assertThat(text.substring(0).asString()).isEqualTo(string);
		assertThat(text.substring(4).asString()).isEqualTo(string.substring(4));

		assertThat(text.substring(string.length() - 1).asString()).isEqualTo("t");
		assertThat(text.substring(string.length() - 1).asString()).isEqualTo(string.substring(string.length() - 1));
		
		
		
		assertThat(text.substring(string.length()).asString()).isEqualTo("");
		assertThat(text.substring(string.length()).asString()).isEqualTo(string.substring(string.length()));
	}

	@Test
	public void testSubstringTwoParamsArgs() {
		
		final String string = "some\n lines\n \nof text";

		final Text text = createText(string);

		try {
			text.substring(-1, 1);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		try {
			text.substring(0, -1);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		try {
			text.substring(-1, -1);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		try {
			text.substring(1, 0);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}

		try {
			text.substring(0, string.length() + 1);
			fail("Expected exception");
		}
		catch (IllegalArgumentException ex) {
		}
	}
	
	@Test
	public void testSubstringTwoParams() {

		final String string = "some\n lines\n \nof text";
		
		final Text text = createText(string);
		
		assertThat(text.substring(0, string.length()).asString()).isEqualTo(string);
		assertThat(text.substring(4, string.length()).asString()).isEqualTo(string.substring(4));

		assertThat(text.substring(4, string.length() - 3).asString()).isEqualTo(string.substring(4, string.length() - 3));

		assertThat(text.substring(string.length() - 1, string.length()).asString()).isEqualTo("t");
		assertThat(text.substring(string.length() - 1, string.length()).asString()).isEqualTo(string.substring(string.length() - 1));
		
		assertThat(text.substring(string.length(), string.length()).asString()).isEqualTo("");
		assertThat(text.substring(string.length(), string.length()).asString()).isEqualTo(string.substring(string.length(), string.length()));
	}

	@Test
	public void testSearchTextForwards() {
		
		final String string = "some\n lines\n \nof text";
		
		final Text text = createText(string);
		
		// case sensitive
		assertThat(text.findForward("som", 		0L, null, true, false, false)).isEqualTo(0L);
		assertThat(text.findForward("lines", 	0L, null, true, false, false)).isEqualTo(6L);
		assertThat(text.findForward("line", 	0L, null, true, false, false)).isEqualTo(6L);
		assertThat(text.findForward("text", 	0L, null, true, false, false)).isEqualTo(17L);
		assertThat(text.findForward("text", 	0L, new TextRange(0L, 17L), true, false, false)).isEqualTo(-1L);
		assertThat(text.findForward("text", 	0L, new TextRange(0L, 21L), true, false, false)).isEqualTo(17L);
		assertThat(text.findForward("\n", 		0L, null, true, false, false)).isEqualTo(4L);

		assertThat(text.findForward("Som", 		0L, null, true, false, false)).isEqualTo(-1L);
		assertThat(text.findForward("Lines", 	0L, null, true, false, false)).isEqualTo(-1L);
		assertThat(text.findForward("Line", 	0L, null, true, false, false)).isEqualTo(-1L);
		assertThat(text.findForward("\n", 		0L, null, true, false, false)).isEqualTo(4L);

		// case insensitive
		assertThat(text.findForward("som", 		0L, null, false, false, false)).isEqualTo(0L);
		assertThat(text.findForward("lines", 	0L, null, false, false, false)).isEqualTo(6L);
		assertThat(text.findForward("line", 	0L, null, false, false, false)).isEqualTo(6L);
		assertThat(text.findForward("\n", 		0L, null, false, false, false)).isEqualTo(4L);

		assertThat(text.findForward("Som", 		0L, null, false, false, false)).isEqualTo(0L);
		assertThat(text.findForward("Lines", 	0L, null, false, false, false)).isEqualTo(6L);
		assertThat(text.findForward("Line", 	0L, null, false, false, false)).isEqualTo(6L);
		assertThat(text.findForward("\n", 		0L, null, false, false, false)).isEqualTo(4L);

		// Whole words
		assertThat(text.findForward("som", 		0L, null, true, false, true)).isEqualTo(-1L);
		assertThat(text.findForward("some", 	0L, null, true, false, true)).isEqualTo(0L);
		assertThat(text.findForward("lines", 	0L, null, true, false, true)).isEqualTo(6L);
		assertThat(text.findForward("line", 	0L, null, true, false, true)).isEqualTo(-1L);
		assertThat(text.findForward("text", 	0L, null, true, false, true)).isEqualTo(17L);
		assertThat(text.findForward("text", 	10L, null, true, false, true)).isEqualTo(17L);
		assertThat(text.findForward("text", 	17L, null, true, false, true)).isEqualTo(17L);
		
		try {
			text.findForward("\n", 0L, null, true, false, true);
			fail("No whitespace in text");
		}
		catch (IllegalArgumentException ex) {
		}
		
		assertThat(text.findForward("text", 0L, null, true, false, true)).isEqualTo(17L);
		
		try {
			text.findForward("of text", 0L, null, true, false, true);
			fail("Expected search text with one word");
		}
		catch (IllegalArgumentException ex) {
		}
		try {
			text.findForward(" ", 0L, null, true, false, true);
			fail("No whitespace in text");
		}
		catch (IllegalArgumentException ex) {
		}

		// Wrapped search
		assertThat(text.findForward("som", 		10L, null, true, true, false)).isEqualTo(0L);
		assertThat(text.findForward("lines", 	15L, null, true, true, false)).isEqualTo(6L);
		assertThat(text.findForward("line", 	15L, null, true, true, false)).isEqualTo(6L);
		assertThat(text.findForward("\n", 		17L, null, true, true, false)).isEqualTo(4L);
		assertThat(text.findForward("lines", 	20L, null, true, true, false)).isEqualTo(6L);
		assertThat(text.findForward(" text", 	17L, null, true, true, false)).isEqualTo(16L);

		assertThat(text.findForward("unknowntext", 10L, null, true, true, false)).isEqualTo(-1L);
	}

	@Test
	public void testSearchTextBackwards() {
		
		final String string = "some\n lines\n \nof text";
		
		final Text text = createText(string);
		
		assertThat(string.length()).isEqualTo(21);
		
		// case sensitive
		assertThat(text.findBackward("som", 	20L, null, true, false, false)).isEqualTo(0L);
		assertThat(text.findBackward("lines", 	20L, null, true, false, false)).isEqualTo(6L);
		assertThat(text.findBackward("line", 	15L, null, true, false, false)).isEqualTo(6L);
		assertThat(text.findBackward("text", 	17L, null, true, false, false)).isEqualTo(-1L);
		assertThat(text.findBackward("text", 	21L, null, true, false, false)).isEqualTo(17L);
		assertThat(text.findBackward("text", 	17L, new TextRange(0L, 17L), true, false, false)).isEqualTo(-1L);
		assertThat(text.findBackward("text", 	21L, new TextRange(0L, 21L), true, false, false)).isEqualTo(17L);
		assertThat(text.findBackward("\n", 		5L,  null, true, false, false)).isEqualTo(4L);

		assertThat(text.findBackward("Som", 	20L, null, true, false, false)).isEqualTo(-1L);
		assertThat(text.findBackward("Lines", 	20L, null, true, false, false)).isEqualTo(-1L);
		assertThat(text.findBackward("Line", 	15L, null, true, false, false)).isEqualTo(-1L);
		assertThat(text.findBackward("\n", 		5L,  null, true, false, false)).isEqualTo(4L);

		// case insensitive
		assertThat(text.findBackward("som", 	20L, null, false, false, false)).isEqualTo(0L);
		assertThat(text.findBackward("lines", 	20L, null, false, false, false)).isEqualTo(6L);
		assertThat(text.findBackward("line", 	15L, null, false, false, false)).isEqualTo(6L);
		assertThat(text.findBackward("\n", 		5L,  null, false, false, false)).isEqualTo(4L);

		assertThat(text.findBackward("Som", 	20L, null, false, false, false)).isEqualTo(0L);
		assertThat(text.findBackward("Lines", 	20L, null, false, false, false)).isEqualTo(6L);
		assertThat(text.findBackward("Line", 	15L, null, false, false, false)).isEqualTo(6L);
		assertThat(text.findBackward("\n", 		5L,  null, false, false, false)).isEqualTo(4L);

		// Whole words
		assertThat(text.findBackward("som", 	20L, null, true, false, true)).isEqualTo(-1L);
		assertThat(text.findBackward("some", 	20L, null, true, false, true)).isEqualTo(0L);
		assertThat(text.findBackward("lines", 	20L, null, true, false, true)).isEqualTo(6L);
		assertThat(text.findBackward("line", 	20L, null, true, false, true)).isEqualTo(-1L);
		assertThat(text.findBackward("text", 	20L, null, true, false, true)).isEqualTo(-1L);
		assertThat(text.findBackward("text", 	21L, null, true, false, true)).isEqualTo(17L);
		
		try {
			text.findBackward("\n", 0L, null, true, false, true);
			fail("No whitespace in text");
		}
		catch (IllegalArgumentException ex) {
		}
		
		assertThat(text.findBackward("text", 21L, null, true, false, true)).isEqualTo(17L);
		
		try {
			text.findBackward("of text", 0L, null, true, false, true);
			fail("Expected search text with one word");
		}
		catch (IllegalArgumentException ex) {
		}
		try {
			text.findBackward(" ", 0L, null, true, false, true);
			fail("No whitespace in text");
		}
		catch (IllegalArgumentException ex) {
		}

		// Wrapped search
		assertThat(text.findBackward("som", 	0L, null, true, true, false)).isEqualTo(0L);
		assertThat(text.findBackward("lines", 	3L, null, true, true, false)).isEqualTo(6L);
		assertThat(text.findBackward("line", 	3L, null, true, true, false)).isEqualTo(6L);
		assertThat(text.findBackward("\n", 		3L, null, true, true, false)).isEqualTo(13L);
		assertThat(text.findBackward("lines", 	5L, null, true, true, false)).isEqualTo(6L);
		assertThat(text.findBackward(" text", 	10L, null, true, true, false)).isEqualTo(16L);

		assertThat(text.findBackward("unknowntext", 10L, null, true, true, false)).isEqualTo(-1L);
	}
}
