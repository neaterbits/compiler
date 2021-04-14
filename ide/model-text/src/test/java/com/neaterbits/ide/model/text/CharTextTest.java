package com.neaterbits.ide.model.text;

import com.neaterbits.ide.util.ui.text.CharText;
import com.neaterbits.ide.util.ui.text.Text;

public class CharTextTest extends BaseTextTest {

	@Override
	protected Text createText(String string) {
		return new CharText(string);
	}
}
