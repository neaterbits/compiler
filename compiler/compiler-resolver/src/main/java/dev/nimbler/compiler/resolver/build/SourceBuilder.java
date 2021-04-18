package dev.nimbler.compiler.resolver.build;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import org.jutils.IntList;
import org.jutils.parse.ParserException;

import dev.nimbler.build.strategies.compilemodules.ParsedModule;
import dev.nimbler.build.strategies.compilemodules.ParsedWithCachedRefs;
import dev.nimbler.compiler.model.common.LanguageSpec;
import dev.nimbler.compiler.model.common.passes.CompilerModel;
import dev.nimbler.compiler.resolver.ResolveError;
import dev.nimbler.compiler.resolver.build.strategies.compilemodules.ParseFileHelper;
import dev.nimbler.compiler.resolver.build.strategies.compilemodules.ResolvePassesHelper;
import dev.nimbler.compiler.util.parse.ParsedFile;
import dev.nimbler.language.codemap.compiler.CompilerCodeMap;
import dev.nimbler.language.codemap.compiler.SynchronizedCompilerCodeMap;

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
