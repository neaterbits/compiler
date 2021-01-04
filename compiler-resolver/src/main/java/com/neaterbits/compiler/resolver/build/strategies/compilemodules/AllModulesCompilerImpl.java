package com.neaterbits.compiler.resolver.build.strategies.compilemodules;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.strategies.compilemodules.AllModulesCompiler;
import com.neaterbits.build.strategies.compilemodules.ParsedModule;
import com.neaterbits.build.strategies.compilemodules.ParsedWithCachedRefs;
import com.neaterbits.build.types.resource.SourceFileResourcePath;
import com.neaterbits.compiler.codemap.compiler.SynchronizedCompilerCodeMap;
import com.neaterbits.compiler.model.common.passes.CompilerModel;
import com.neaterbits.compiler.resolver.ResolveError;
import com.neaterbits.compiler.resolver.build.CompilerOptions;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.compiler.util.parse.Parser;
import com.neaterbits.util.IntList;
import com.neaterbits.util.parse.ParserException;

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
