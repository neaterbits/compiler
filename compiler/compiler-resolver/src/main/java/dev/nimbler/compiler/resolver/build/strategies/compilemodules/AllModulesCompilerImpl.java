package dev.nimbler.compiler.resolver.build.strategies.compilemodules;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

import org.jutils.IntList;
import org.jutils.parse.ParserException;

import dev.nimbler.build.strategies.compilemodules.AllModulesCompiler;
import dev.nimbler.build.strategies.compilemodules.ParsedModule;
import dev.nimbler.build.strategies.compilemodules.ParsedWithCachedRefs;
import dev.nimbler.build.types.resource.SourceFileResourcePath;
import dev.nimbler.compiler.model.common.passes.CompilerModel;
import dev.nimbler.compiler.resolver.ResolveError;
import dev.nimbler.compiler.resolver.build.CompilerOptions;
import dev.nimbler.compiler.util.parse.ParsedFile;
import dev.nimbler.compiler.util.parse.Parser;
import dev.nimbler.language.codemap.compiler.SynchronizedCompilerCodeMap;

public final class AllModulesCompilerImpl<PARSED_FILE extends ParsedFile, COMPILATION_UNIT>
    implements AllModulesCompiler<PARSED_FILE, SynchronizedCompilerCodeMap, ResolveError> {
        
    private final Parser<COMPILATION_UNIT> parser;
    private final CompilerModel<COMPILATION_UNIT, PARSED_FILE> compilerModel;
    private final CompilerOptions options;
    
    public AllModulesCompilerImpl(
            Parser<COMPILATION_UNIT> parser,
            CompilerModel<COMPILATION_UNIT, PARSED_FILE> compilerModel,
            CompilerOptions options) {
        
        Objects.requireNonNull(parser);
        Objects.requireNonNull(compilerModel);
        Objects.requireNonNull(options);
        
        this.parser = parser;
        this.compilerModel = compilerModel;
        this.options = options;
    }

    @Override
    public ParsedWithCachedRefs<PARSED_FILE, ResolveError> parseFile(
            SourceFileResourcePath sourceFile,
            Charset charset,
            SynchronizedCompilerCodeMap codeMap)
                            throws IOException, ParserException {

        final File file = sourceFile.getFile();
        
        final IntList types = new IntList();
        
        final ParsedWithCachedRefs<PARSED_FILE, ResolveError> parsed;
        
        try (FileInputStream stream = new FileInputStream(file)) {
            
            parsed = ParseFileHelper.parse(
                    stream,
                    charset,
                    file.getName(),
                    file,
                    parser,
                    types,
                    codeMap,
                    compilerModel);
        }

        return parsed;
    }
    
    
    @Override
    public List<ResolveError> resolveParseTreeInPlaceFromCodeMap(
                                            ParsedModule<PARSED_FILE, ResolveError> module,
                                            SynchronizedCompilerCodeMap codeMap) {
        
        return ResolvePassesHelper.resolveParseTreeInPlaceFromCodeMap(
                    module,
                    compilerModel.getCompilationUnitModel(),
                    codeMap,
                    options);
    }
}
