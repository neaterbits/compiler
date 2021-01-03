    package com.neaterbits.compiler.parser.java;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.neaterbits.compiler.ast.encoded.EncodedCompilationUnit;
import com.neaterbits.compiler.ast.objects.ASTParseTreeFactory;
import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.compiler.language.java.JavaLanguageSpec;
import com.neaterbits.compiler.language.java.JavaTypes;
import com.neaterbits.compiler.language.java.parser.listener.stackbased.JavaIterativeListener;
import com.neaterbits.compiler.parser.listener.common.IterativeParseTreeListener;
import com.neaterbits.compiler.resolver.build.LanguageCompiler;
import com.neaterbits.compiler.util.FullContextProvider;
import com.neaterbits.compiler.util.StringSourceFullContextProvider;
import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.util.io.strings.StringSource;
import com.neaterbits.util.parse.ParserException;

public class TestJavaParserWithEncodeParserListener extends BaseJavaParserTest {

    @SuppressWarnings("unchecked")
    @Override
    CompilationUnit parse(String source) throws IOException, ParserException {

        final String fileName = "testfile";
        
        final InputStream inputStream = new ByteArrayInputStream(source.getBytes());

        final JavaRecursiveParserHelper<EncodedCompilationUnit> parser = new JavaRecursiveParserHelper<>((file, stringBuffers) -> {

            final IterativeParseTreeListener<EncodedCompilationUnit> listener
                = new JavaEncodedParserListener(file, stringBuffers);

            return listener;
        });

        final EncodedCompilationUnit encodedCompilationUnit = parser.parse(fileName, inputStream);

        final ParseLogger logger = new ParseLogger(System.out, encodedCompilationUnit.getFullContextProvider());
        
        final IntCompilerCodeMap codeMap = new IntCompilerCodeMap();
        
        LanguageCompiler.addBuiltinTypesToCodeMap(JavaLanguageSpec.INSTANCE, codeMap);

        final ASTParseTreeFactory parseTreeFactory = new ASTParseTreeFactory(
                JavaTypes.getBuiltinTypes(),
                codeMap::getTypeNoByTypeName);

        final StringSource stringSource = new StringSource() {

            @Override
            public String asString(long stringRef) {

                return encodedCompilationUnit.getStringFromRef((int)stringRef);
            }

            @Override
            public String asStringFromOffset(int startOffset, int endOffset) {
                throw new UnsupportedOperationException();
            }
        };
        
        final FullContextProvider fullContextProvider = new StringSourceFullContextProvider(fileName, stringSource);

        final JavaIterativeListener astListener = new JavaIterativeListener(
                stringSource,
                encodedCompilationUnit.getContextAccess(),
                fullContextProvider,
                logger,
                parseTreeFactory);

        return (CompilationUnit)encodedCompilationUnit.iterate(astListener);
    }
}
