package dev.nimbler.compiler.language.java.parser;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Test;
import org.jutils.parse.ParserException;

public class TestSourceFile {

    @Test
    public void parseFile() throws IOException, ParserException {
        
        final String file = System.getProperty("user.home")
                + "/projects/compiler"
                + "/compiler-util/src/main/java"
                + "/com/neaterbits/compiler/util/ContextScopedName.java";
        
        final ObjectJavaParser parser = new ObjectJavaParser();
        
        try (FileInputStream inputStream = new FileInputStream(file)) {
        
            parser.parse("thefile", inputStream);
        }
    }
}
