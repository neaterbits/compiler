package dev.nimbler.compiler.language.java.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.neaterbits.util.parse.ParserException;

import dev.nimbler.compiler.ast.objects.CompilationUnit;
import dev.nimbler.compiler.language.java.JavaLanguageSpec;
import dev.nimbler.compiler.parser.java.recursive.JavaRecursiveParserHelper;
import dev.nimbler.compiler.resolver.build.ModulesBuilder;
import dev.nimbler.language.codemap.compiler.IntCompilerCodeMap;

public class TestJavaParserWithObjectParserListener extends BaseJavaParserTest {
        
    @Override
    CompilationUnit parse(String source) throws IOException, ParserException {

        final InputStream inputStream = new ByteArrayInputStream(source.getBytes());
        
        final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();
        
        ModulesBuilder.addBuiltinTypesToCodeMap(JavaLanguageSpec.INSTANCE, codeMap);

        final JavaRecursiveParserHelper<CompilationUnit> parser = new ObjectJavaParser(codeMap::getTypeNoByTypeName);
        
        return parser.parse("testfile", inputStream);
    }
}
