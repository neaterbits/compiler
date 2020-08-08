package com.neaterbits.compiler.java;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.parser.ASTParsedFile;
import com.neaterbits.compiler.resolver.passes.CodeMapCompiledAndMappedFiles;
import com.neaterbits.compiler.resolver.util.CompilerLanguage;
import com.neaterbits.compiler.util.parse.Parser;

public abstract class BaseCompilerTest
                    extends BaseGenericCompilerTest<CompilationUnit, ASTParsedFile> {

    @Override
    protected Parser<CompilationUnit> createParser() {

        return new JavaLexerObjectParser();
    }

    @Override
    protected CompilerLanguage<
                        CompilationUnit,
                        ASTParsedFile,
                        CodeMapCompiledAndMappedFiles<CompilationUnit>>
            
            createCompilerLanguage(Parser<CompilationUnit> parser) {

        return new JavaCompilerLanguage();
    }
}
