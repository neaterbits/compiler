package com.neaterbits.compiler.java.compile;

import java.io.IOException;
import java.util.List;

import com.neaterbits.compiler.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.compiler.java.compile.CompileFileCollector.CompileFile;
import com.neaterbits.compiler.resolver.util.CompilerLanguage;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.util.parse.ParserException;

public abstract class TestFilesBuilder<COMPILATION_UNIT, PARSED_FILE extends ParsedFile> {

    protected abstract CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> compileFiles(
            List<CompileFile> files,
            IntCompilerCodeMap codeMap,
            CompilerLanguage<COMPILATION_UNIT, PARSED_FILE> compilerLanguage) throws IOException, ParserException;
}
