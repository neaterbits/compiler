    package dev.nimbler.compiler.language.java.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.neaterbits.util.io.strings.StringSource;
import com.neaterbits.util.parse.ParserException;

import dev.nimbler.compiler.ast.encoded.EncodedCompilationUnit;
import dev.nimbler.compiler.ast.objects.ASTParseTreeFactory;
import dev.nimbler.compiler.ast.objects.CompilationUnit;
import dev.nimbler.compiler.language.java.JavaLanguageSpec;
import dev.nimbler.compiler.language.java.JavaTypes;
import dev.nimbler.compiler.language.java.parser.listener.stackbased.JavaIterativeListener;
import dev.nimbler.compiler.parser.java.recursive.JavaEncodedParserListener;
import dev.nimbler.compiler.parser.java.recursive.JavaRecursiveParserHelper;
import dev.nimbler.compiler.parser.listener.common.IterativeParseTreeListener;
import dev.nimbler.compiler.resolver.build.ModulesBuilder;
import dev.nimbler.compiler.util.FullContextProvider;
import dev.nimbler.compiler.util.StringSourceFullContextProvider;
import dev.nimbler.compiler.util.parse.ParseLogger;
import dev.nimbler.language.codemap.compiler.IntCompilerCodeMap;

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
        
        ModulesBuilder.addBuiltinTypesToCodeMap(JavaLanguageSpec.INSTANCE, codeMap);

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
