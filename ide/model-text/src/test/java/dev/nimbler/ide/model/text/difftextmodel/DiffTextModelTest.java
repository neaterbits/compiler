package dev.nimbler.ide.model.text.difftextmodel;

import dev.nimbler.ide.model.text.BaseTextModelTest;
import dev.nimbler.ide.model.text.TextModel;
import dev.nimbler.ide.util.ui.text.LineDelimiter;
import dev.nimbler.ide.util.ui.text.StringText;

public class DiffTextModelTest extends BaseTextModelTest {

	@Override
	protected TextModel makeTextModel(LineDelimiter lineDelimiter, StringText initialText) {
		return new DiffTextModel(lineDelimiter, initialText);
	}
}
