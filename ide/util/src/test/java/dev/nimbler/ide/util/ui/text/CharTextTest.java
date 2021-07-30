package dev.nimbler.ide.util.ui.text;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class CharTextTest extends BaseTextTest {

    @Test
    public void testCharTextEquals() {
     
        assertThat(new CharText("123")).isEqualTo(new CharText("123"));
        
        assertThat(new CharText("123")).isNotEqualTo(new CharText("1234"));
        
        assertThat(new CharText("123")).isNotEqualTo(new CharText("12"));

        assertThat(new CharText("123")).isNotEqualTo(null);

        assertThat(new CharText("123")).isNotEqualTo(new CharText("12"));
        assertThat(new CharText("123")).isEqualTo(new StringText("123"));
    }

    @Override
    Text text(String string) {

        return new CharText(string);
    }
}
