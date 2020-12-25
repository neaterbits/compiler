package com.neaterbits.compiler.java;

import java.io.IOException;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.parser.ASTParsedFile;
import com.neaterbits.compiler.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.compiler.java.resolve.TestResolvedTypes;
import com.neaterbits.compiler.language.java.JavaLanguageSpec;
import com.neaterbits.compiler.resolver.build.LanguageCompiler;
import com.neaterbits.compiler.resolver.util.CompilerLanguage;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.parse.Parser;
import com.neaterbits.util.parse.ParserException;

public abstract class BaseCompilerTest
                    extends BaseGenericCompilerTest<CompilationUnit, ASTParsedFile> {

    private final IntCompilerCodeMap codeMap;
    
    protected BaseCompilerTest() {
        
        this.codeMap = new IntCompilerCodeMap();
        
        LanguageCompiler.addBuiltinTypesToCodeMap(JavaLanguageSpec.INSTANCE, codeMap);
    }

    @Override
    protected final Parser<CompilationUnit> createParser() {

        final CreateParserListener<CompilationUnit> createParserListener
            = ObjectJavaParser.createListener(codeMap::getTypeNoByTypeName);
        
        return new JavaLexerObjectParser<>(createParserListener);
    }

    @Override
    protected final CompilerLanguage<CompilationUnit, ASTParsedFile>
            createCompilerLanguage() {

        return new JavaCompilerLanguage();
    }

    protected final CodeMapCompiledAndMappedFiles<CompilationUnit> compileAndMap(
            FileSpec fileSpec,
            String text,
            TestResolvedTypes resolvedTypes) throws IOException, ParserException {
    
        return compileAndMap(fileSpec, text, resolvedTypes, codeMap);
    }
    
}
