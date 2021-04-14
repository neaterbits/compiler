package dev.nimbler.compiler.language.java.compile;

import java.io.IOException;

import com.neaterbits.util.parse.ParserException;

import dev.nimbler.compiler.ast.objects.ASTParseTreeFactory.GetBuiltinTypeNo;
import dev.nimbler.compiler.ast.objects.CompilationUnit;
import dev.nimbler.compiler.ast.objects.parser.ASTParsedFile;
import dev.nimbler.compiler.language.java.JavaCompilerLanguage;
import dev.nimbler.compiler.language.java.TestFile;
import dev.nimbler.compiler.language.java.resolve.TestResolvedTypes;
import dev.nimbler.compiler.resolver.util.CompilerLanguage;
import dev.nimbler.language.codemap.compiler.IntCompilerCodeMap;

public abstract class BaseCompilerTest extends BaseGenericCompilerTest<CompilationUnit, ASTParsedFile> {

    @Override
    protected final CompilerLanguage<CompilationUnit, ASTParsedFile>
            createCompilerLanguage(GetBuiltinTypeNo getBuiltinTypeNo) {

        return new JavaCompilerLanguage(getBuiltinTypeNo);
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
