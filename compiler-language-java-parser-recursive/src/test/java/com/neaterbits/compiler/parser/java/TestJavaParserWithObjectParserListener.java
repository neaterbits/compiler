package com.neaterbits.compiler.parser.java;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.util.parse.ParserException;

public class TestJavaParserWithObjectParserListener extends BaseJavaParserTest {
        
    @Override
    CompilationUnit parse(String source) throws IOException, ParserException {

        final InputStream inputStream = new ByteArrayInputStream(source.getBytes());
        
        final JavaParser<CompilationUnit> parser = new ObjectJavaParser();
        
        return parser.parse("testfile", inputStream);
    }
}
