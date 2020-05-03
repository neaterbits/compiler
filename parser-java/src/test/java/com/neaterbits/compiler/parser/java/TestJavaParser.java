package com.neaterbits.compiler.parser.java;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class TestJavaParser {

    @Test
    public void testParse() throws IOException, ParseException {
        
        final JavaParser<Void> parser = new JavaParser<>();
        
        
        final String code = "package com.test;\n"
                + ""
                + "import com.test.importtest.TestClass;\n";
        
        final InputStream inputStream = new ByteArrayInputStream(code.getBytes());
        
        parser.parse("testfile", inputStream);
        
    }
    
}
