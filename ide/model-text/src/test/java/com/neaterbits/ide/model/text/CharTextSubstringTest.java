package com.neaterbits.ide.model.text;

import com.neaterbits.ide.util.ui.text.CharText;
import com.neaterbits.ide.util.ui.text.Text;

public class CharTextSubstringTest extends BaseTextTest {

	@Override
	protected Text createText(String string) {
		return new CharText("prefix" + string + "postfix")
				.substring(
						"prefix".length(),
						"prefix".length() + string.length());
	}
}
