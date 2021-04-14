package com.neaterbits.compiler.resolver.build;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import com.neaterbits.build.strategies.compilemodules.ParsedModule;
import com.neaterbits.build.strategies.compilemodules.ParsedWithCachedRefs;
import com.neaterbits.compiler.model.common.LanguageSpec;
import com.neaterbits.compiler.model.common.passes.CompilerModel;
import com.neaterbits.compiler.resolver.ResolveError;
import com.neaterbits.compiler.resolver.build.strategies.compilemodules.ParseFileHelper;
import com.neaterbits.compiler.resolver.build.strategies.compilemodules.ResolvePassesHelper;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.language.codemap.compiler.CompilerCodeMap;
import com.neaterbits.language.codemap.compiler.SynchronizedCompilerCodeMap;
import com.neaterbits.util.IntList;
import com.neaterbits.util.parse.ParserException;

public final class SourceBuilder<COMPILATION_UNIT, PARSED_FILE extends ParsedFile>
    extends BaseBuilder<COMPILATION_UNIT, PARSED_FILE> {

    public SourceBuilder(
            LanguageSpec languageSpec,
            CompilerModel<COMPILATION_UNIT, PARSED_FILE> compilerModel,
            CompilerOptions options) {
        
        super(languageSpec, compilerModel, options);
    }

    public ResolvedSourceModule<PARSED_FILE>
    compile(
            SourceModule inputModule,
            CompilerCodeMap intCodeMap,
            Function<CompileSource, File> createFile) throws IOException, ParserException {

        Objects.requireNonNull(inputModule);
        Objects.requireNonNull(intCodeMap);

        final SynchronizedCompilerCodeMap codeMap = new SynchronizedCompilerCodeMap(intCodeMap);

        final List<ParsedWithCachedRefs<PARSED_FILE, ResolveError>> parsedFiles
            = new ArrayList<>(inputModule.getSources().size());

        for (CompileSource input : inputModule.getSources()) {

            final IntList types = new IntList();
            
            final byte [] bytes = input.getSource().getBytes(inputModule.getCharset());
            
            final InputStream stream
                = new ByteArrayInputStream(bytes);
            
            final ParsedWithCachedRefs<PARSED_FILE, ResolveError> parsed

                = ParseFileHelper.parse(
                    stream,
                    inputModule.getCharset(),
                    input.getFileName(),
                    createFile.apply(input),
                    getParser(),
                    types,
                    codeMap,
                    getCompilerModel());

            parsedFiles.add(parsed);
        }

        final ParsedModule<PARSED_FILE, ResolveError> parsedModule
            = new ParsedModule<>(parsedFiles);

        final List<ResolveError> resolveErrors
            = ResolvePassesHelper.resolveParseTreeInPlaceFromCodeMap(
                parsedModule,
                getCompilerModel().getCompilationUnitModel(),
                codeMap,
                getOptions());

        return new ResolvedSourceModule<>(
                inputModule,
                parsedModule,
                resolveErrors);
    }
}
