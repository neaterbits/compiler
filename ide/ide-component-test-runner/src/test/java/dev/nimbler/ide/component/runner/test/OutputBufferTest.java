package dev.nimbler.ide.component.runner.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class OutputBufferTest {
    
    @Test
    public void testOutputBuffer() {
        
        final OutputBuffer outputBuffer = new OutputBuffer();
        
        assertThat(outputBuffer.retrieveLines()).isNull();
        
        outputBuffer.add("123");

        assertThat(outputBuffer.retrieveLines()).isNull();

        outputBuffer.add("456\r\n");

        assertThat(outputBuffer.retrieveLines()).isEqualTo("123456\r\n");

        outputBuffer.add("abc\r\ndef\r\nghi");
        assertThat(outputBuffer.retrieveLines()).isEqualTo("abc\r\ndef\r\n");
        assertThat(outputBuffer.retrieveLines()).isNull();

        outputBuffer.add("\r\njkl");
        assertThat(outputBuffer.retrieveLines()).isEqualTo("ghi\r\n");
        assertThat(outputBuffer.retrieveLines()).isNull();
        
        outputBuffer.reset();

        outputBuffer.add("xyz\r\n");
        assertThat(outputBuffer.retrieveLines()).isEqualTo("xyz\r\n");
        assertThat(outputBuffer.retrieveLines()).isNull();
    }

    @Test
    public void testKeepsInitialEmptyLine() {

        final OutputBuffer outputBuffer = new OutputBuffer();
        
        assertThat(outputBuffer.retrieveLines()).isNull();
        
        outputBuffer.add("\r\n");
        assertThat(outputBuffer.retrieveLines()).isNull();

        outputBuffer.add("123");
        assertThat(outputBuffer.retrieveLines()).isNull();
        
        outputBuffer.add("\r\n");
        assertThat(outputBuffer.retrieveLines()).isEqualTo("\r\n123\r\n");
    }

    @Test
    public void testKeepsTrailingEmptyLine() {

        final OutputBuffer outputBuffer = new OutputBuffer();
        
        assertThat(outputBuffer.retrieveLines()).isNull();
        
        outputBuffer.add("123\r\n\r\n");
        assertThat(outputBuffer.retrieveLines()).isEqualTo("123\r\n");

        outputBuffer.add("234");
        assertThat(outputBuffer.retrieveLines()).isNull();
        
        outputBuffer.add("\r\n");
        assertThat(outputBuffer.retrieveLines()).isEqualTo("\r\n234\r\n");
    }
}
