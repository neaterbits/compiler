package com.neaterbits.ide.model.text.difftextmodel;

import com.neaterbits.ide.model.text.TextModel;
import com.neaterbits.ide.model.text.BaseTextModelTest;
import com.neaterbits.ide.util.ui.text.LineDelimiter;
import com.neaterbits.ide.util.ui.text.StringText;

public class DiffTextModelTest extends BaseTextModelTest {

	@Override
	protected TextModel makeTextModel(LineDelimiter lineDelimiter, StringText initialText) {
		return new DiffTextModel(lineDelimiter, initialText);
	}
}
