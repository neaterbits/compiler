package com.neaterbits.compiler.java;

import java.io.IOException;
import java.util.Collection;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.parser.recursive.InputLexerParser;
import com.neaterbits.compiler.util.parse.ParseError;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.io.strings.StringSourceInputStream;
import com.neaterbits.util.parse.ParserException;

public class JavaLexerObjectParser
    extends InputLexerParser<CompilationUnit, IterativeParserListener<CompilationUnit>> {

    @Override
    protected CompilationUnit parse(
            StringSourceInputStream stream,
            Collection<ParseError> errors,
            String file,
            ParseLogger parseLogger) throws IOException, ParserException {

        final ObjectJavaParser parser = new ObjectJavaParser();
        
        return parser.parse(file, stream);
    }
}
