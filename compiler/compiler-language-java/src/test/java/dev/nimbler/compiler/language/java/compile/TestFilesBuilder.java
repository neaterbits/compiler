package dev.nimbler.compiler.language.java.compile;

import java.io.IOException;
import java.util.List;

import com.neaterbits.util.parse.ParserException;

import dev.nimbler.compiler.language.java.compile.CompileFileCollector.CompileFile;
import dev.nimbler.compiler.resolver.util.CompilerLanguage;
import dev.nimbler.compiler.util.parse.ParsedFile;
import dev.nimbler.language.codemap.compiler.IntCompilerCodeMap;

public abstract class TestFilesBuilder<COMPILATION_UNIT, PARSED_FILE extends ParsedFile> {

    protected abstract CodeMapCompiledAndMappedFiles<COMPILATION_UNIT> compileFiles(
            List<CompileFile> files,
            IntCompilerCodeMap codeMap,
            CompilerLanguage<COMPILATION_UNIT, PARSED_FILE> compilerLanguage) throws IOException, ParserException;
}
