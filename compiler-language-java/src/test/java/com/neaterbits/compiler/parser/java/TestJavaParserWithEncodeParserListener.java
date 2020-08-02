    package com.neaterbits.compiler.parser.java;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.neaterbits.compiler.ast.encoded.EncodedCompilationUnit;
import com.neaterbits.compiler.ast.objects.ASTParseTreeFactory;
import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.language.java.JavaTypes;
import com.neaterbits.compiler.language.java.parser.listener.stackbased.JavaIterativeListener;
import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.io.strings.StringSource;
import com.neaterbits.util.parse.ParserException;

public class TestJavaParserWithEncodeParserListener extends BaseJavaParserTest {

    @SuppressWarnings("unchecked")
    @Override
    CompilationUnit parse(String source) throws IOException, ParserException {

        final InputStream inputStream = new ByteArrayInputStream(source.getBytes());
        
        final JavaParser<EncodedCompilationUnit> parser = new JavaParser<>(stringBuffers -> {
            
            final IterativeParserListener<EncodedCompilationUnit> listener
                = new JavaEncodedParserListener("testfile", stringBuffers);
            
            return listener;
        });
        
        final EncodedCompilationUnit encodedCompilationUnit = parser.parse("testfile", inputStream);
        
        final ParseLogger logger = new ParseLogger(System.out, encodedCompilationUnit.getFullContextProvider());
        
        final ASTParseTreeFactory parseTreeFactory = new ASTParseTreeFactory(JavaTypes.getBuiltinTypes());
        
        final StringSource stringSource = new StringSource() {
            
            @Override
            public String asString(long stringRef) {

                return encodedCompilationUnit.getStringFromRef((int)stringRef);
            }
        };
        
        final JavaIterativeListener astListener = new JavaIterativeListener(
                stringSource,
                encodedCompilationUnit.getContextAccess(),
                logger,
                parseTreeFactory);
        
        return (CompilationUnit)encodedCompilationUnit.iterate(astListener);
    }
}
