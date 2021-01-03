package com.neaterbits.compiler.parser.java;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.compiler.java.ObjectJavaParser;
import com.neaterbits.compiler.language.java.JavaLanguageSpec;
import com.neaterbits.compiler.resolver.build.LanguageCompiler;
import com.neaterbits.util.parse.ParserException;

public class TestJavaParserWithObjectParserListener extends BaseJavaParserTest {
        
    @Override
    CompilationUnit parse(String source) throws IOException, ParserException {

        final InputStream inputStream = new ByteArrayInputStream(source.getBytes());
        
        final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();
        
        LanguageCompiler.addBuiltinTypesToCodeMap(JavaLanguageSpec.INSTANCE, codeMap);

        final JavaParser<CompilationUnit> parser = new ObjectJavaParser(codeMap::getTypeNoByTypeName);
        
        return parser.parse("testfile", inputStream);
    }
}
