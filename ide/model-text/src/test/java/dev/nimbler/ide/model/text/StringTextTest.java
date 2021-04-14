package dev.nimbler.ide.model.text;

import dev.nimbler.ide.util.ui.text.StringText;
import dev.nimbler.ide.util.ui.text.Text;

public class StringTextTest extends BaseTextTest {

	@Override
	protected Text createText(String string) {
		return new StringText(string);
	}
}
