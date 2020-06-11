package com.neaterbits.compiler.parser.java;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import com.neaterbits.compiler.ast.objects.ASTParseTreeFactory;
import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.language.java.parser.listener.stackbased.JavaIterativeListener;
import com.neaterbits.compiler.language.java.parser.listener.stackbased.JavaTypes;
import com.neaterbits.compiler.parser.listener.common.IterativeParserListener;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.io.strings.StringSource;
import com.neaterbits.util.parse.ParserException;

public class TestJavaParserWithObjectParserListener extends BaseJavaParserTest {
        
    @Override
    CompilationUnit parse(String source) throws IOException, ParserException {

        final InputStream inputStream = new ByteArrayInputStream(source.getBytes());
        
        final ParseLogger logger = new ParseLogger(System.out);
        final ASTParseTreeFactory parseTreeFactory = new ASTParseTreeFactory(JavaTypes.getBuiltinTypes());
        
        final JavaParser<CompilationUnit> parser = new JavaParser<>(stringBuffers -> {
            
            final StringSource stringSource = new StringSource() {
                
                @Override
                public String asString(long stringRef) {
                    return stringBuffers.getString(stringRef);
                }
                
                @Override
                public Integer asInteger(long stringRef) {
                    
                    return Integer.parseInt(asString(stringRef));
                }
                
                @Override
                public int asInt(long stringRef) {

                    return Integer.parseInt(asString(stringRef));
                }

                @Override
                public BigDecimal asBigDecimal(long stringRef) {
                    return new BigDecimal(asString(stringRef));
                }
            };
              
            @SuppressWarnings("unchecked")
            final IterativeParserListener<CompilationUnit> listener = new JavaIterativeListener(stringSource, logger, parseTreeFactory);
            
            return listener;
        });
        
        return parser.parse("testfile", inputStream);
    }
}
