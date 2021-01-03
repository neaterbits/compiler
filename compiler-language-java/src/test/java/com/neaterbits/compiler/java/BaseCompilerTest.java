package com.neaterbits.compiler.java;

import java.io.IOException;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.parser.ASTParsedFile;
import com.neaterbits.compiler.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.compiler.java.resolve.TestResolvedTypes;
import com.neaterbits.compiler.language.java.JavaLanguageSpec;
import com.neaterbits.compiler.parser.listener.common.CreateParserListener;
import com.neaterbits.compiler.resolver.build.LanguageCompiler;
import com.neaterbits.compiler.resolver.util.CompilerLanguage;
import com.neaterbits.compiler.util.parse.Parser;
import com.neaterbits.util.parse.ParserException;

public abstract class BaseCompilerTest extends BaseGenericCompilerTest<CompilationUnit, ASTParsedFile> {

    @Override
    protected final Parser<CompilationUnit> createParser(IntCompilerCodeMap codeMap) {

        LanguageCompiler.addBuiltinTypesToCodeMap(JavaLanguageSpec.INSTANCE, codeMap);

        final CreateParserListener<CompilationUnit> createParserListener
            = JavaUtil.createListener(codeMap::getTypeNoByTypeName);
        
        return new JavaLexerObjectParser<>(createParserListener);
    }

    @Override
    protected final CompilerLanguage<CompilationUnit, ASTParsedFile>
            createCompilerLanguage() {

        return new JavaCompilerLanguage();
    }

    protected final CodeMapCompiledAndMappedFiles<CompilationUnit>
    compileAndMap(TestFile testFile, TestResolvedTypes resolvedTypes) throws IOException, ParserException {

        return compileAndMap(testFile.getName(), testFile.getText(), resolvedTypes);
    }
            

    protected final CodeMapCompiledAndMappedFiles<CompilationUnit> compileAndMap(
            String name,
            String text,
            TestResolvedTypes resolvedTypes) throws IOException, ParserException {
    
        return compileAndMap(name, text, resolvedTypes, new IntCompilerCodeMap());
    }
}
