package dev.nimbler.ide.model.text;

import dev.nimbler.ide.util.ui.text.CharText;
import dev.nimbler.ide.util.ui.text.Text;

public class CharTextTest extends BaseTextTest {

	@Override
	protected Text createText(String string) {
		return new CharText(string);
	}
}
