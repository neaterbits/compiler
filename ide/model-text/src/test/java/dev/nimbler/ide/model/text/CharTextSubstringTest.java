package dev.nimbler.ide.model.text;

import dev.nimbler.ide.util.ui.text.CharText;
import dev.nimbler.ide.util.ui.text.Text;

public class CharTextSubstringTest extends BaseTextTest {

	@Override
	protected Text createText(String string) {
		return new CharText("prefix" + string + "postfix")
				.substring(
						"prefix".length(),
						"prefix".length() + string.length());
	}
}
