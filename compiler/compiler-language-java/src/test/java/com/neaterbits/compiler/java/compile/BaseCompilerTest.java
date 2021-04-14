package com.neaterbits.compiler.java.compile;

import java.io.IOException;

import com.neaterbits.compiler.ast.objects.ASTParseTreeFactory.GetBuiltinTypeNo;
import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.ast.objects.parser.ASTParsedFile;
import com.neaterbits.compiler.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.compiler.java.JavaCompilerLanguage;
import com.neaterbits.compiler.java.TestFile;
import com.neaterbits.compiler.java.resolve.TestResolvedTypes;
import com.neaterbits.compiler.resolver.util.CompilerLanguage;
import com.neaterbits.util.parse.ParserException;

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
