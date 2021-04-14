package com.neaterbits.ide.model.text;


import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.ide.util.ui.text.LineDelimiter;
import com.neaterbits.ide.util.ui.text.StringText;

public class StringTextModelTest extends BaseTextModelTest {

	@Override
	protected TextModel makeTextModel(LineDelimiter lineDelimiter, StringText initialText) {
		return new StringTextModel(lineDelimiter, initialText);
	}
	
	@Test
	public void testRemove() {
		
		final TextModel textModel = new StringTextModel(UnixLineDelimiter.INSTANCE, "some text");
		
		textModel.replaceTextRange(3, 1, new StringText(""));
		
		assertThat(textModel.getText().asString()).isEqualTo("som text");
	}
}
