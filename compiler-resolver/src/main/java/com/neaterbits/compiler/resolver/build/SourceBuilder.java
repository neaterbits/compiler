package com.neaterbits.compiler.resolver.build;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.neaterbits.build.strategies.compilemodules.ParsedModule;
import com.neaterbits.build.strategies.compilemodules.ParsedWithCachedRefs;
import com.neaterbits.compiler.codemap.compiler.IntCompilerCodeMap;
import com.neaterbits.compiler.codemap.compiler.SynchronizedCompilerCodeMap;
import com.neaterbits.compiler.model.common.LanguageSpec;
import com.neaterbits.compiler.model.common.passes.CompilerModel;
import com.neaterbits.compiler.resolver.ResolveError;
import com.neaterbits.compiler.resolver.build.strategies.compilemodules.ParseFileHelper;
import com.neaterbits.compiler.resolver.build.strategies.compilemodules.ResolvePassesHelper;
import com.neaterbits.compiler.util.parse.ParsedFile;
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

    public ParsedWithCachedRefs<PARSED_FILE, ResolveError>
    compile(SourceModule input, IntCompilerCodeMap intCodeMap) throws IOException, ParserException {
        
        Objects.requireNonNull(input);
        
        final SynchronizedCompilerCodeMap codeMap = new SynchronizedCompilerCodeMap(intCodeMap);

        final IntList types = new IntList();
        
        final byte [] bytes = input.getSource().getBytes(input.getCharset());
        
        final InputStream stream
            = new ByteArrayInputStream(bytes);
        
        final ParsedWithCachedRefs<PARSED_FILE, ResolveError> parsed
            = ParseFileHelper.parse(
                stream,
                input.getCharset(),
                input.getFileName(),
                null,
                getParser(),
                types,
                codeMap,
                getCompilerModel());
        
        final ParsedModule<PARSED_FILE, ResolveError> module
            = new ParsedModule<>(Arrays.asList(parsed));
        
        final List<ResolveError> resolveErrors
            = ResolvePassesHelper.resolveParseTreeInPlaceFromCodeMap(
                module,
                getCompilerModel().getCompilationUnitModel(),
                codeMap,
                getOptions());

        parsed.getResolveErrorsList().addAll(resolveErrors);
        
        return parsed;
    }
}
