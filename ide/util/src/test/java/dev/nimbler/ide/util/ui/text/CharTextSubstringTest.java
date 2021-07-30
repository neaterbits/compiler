package dev.nimbler.ide.util.ui.text;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class CharTextSubstringTest extends BaseTextTest {

    @Test
    public void testCharTextSubstringEquals() {
     
        assertThat(new CharTextSubstring(new CharText("1234"), 0, 3))
            .isEqualTo(new CharText("123"));
        
        assertThat(new CharTextSubstring(new CharText("123"), 0, 3))
            .isNotEqualTo(new CharText("1234"));
        
        assertThat(new CharTextSubstring(new CharText("123"), 0, 3))
            .isNotEqualTo(new CharText("12"));

        assertThat(new CharTextSubstring(new CharText("123"), 0, 3))
            .isNotEqualTo(null);

        assertThat(new CharTextSubstring(new CharText("123"), 0, 3))
            .isNotEqualTo(new CharText("12"));

        assertThat(new CharTextSubstring(new CharText("123"), 0, 3))
            .isEqualTo(new StringText("123"));
    }

	@Override
	protected Text text(String string) {
		return new CharText("prefix" + string + "postfix")
				.substring(
						"prefix".length(),
						"prefix".length() + string.length());
	}
}
