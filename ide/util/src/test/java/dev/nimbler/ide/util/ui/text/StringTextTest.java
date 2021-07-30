package dev.nimbler.ide.util.ui.text;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class StringTextTest extends BaseTextTest {

    @Test
    public void testStringTextEquals() {
     
        assertThat(new StringText("123")).isEqualTo(new StringText("123"));
        
        assertThat(new StringText("123")).isNotEqualTo(new StringText("1234"));
        
        assertThat(new StringText("123")).isNotEqualTo(new StringText("12"));

        assertThat(new StringText("123")).isNotEqualTo(null);

        assertThat(new StringText("123")).isNotEqualTo(new CharText("12"));
        assertThat(new StringText("123")).isEqualTo(new CharText("123"));
    }

    @Override
    Text text(String string) {

        return new StringText(string);
    }
}
