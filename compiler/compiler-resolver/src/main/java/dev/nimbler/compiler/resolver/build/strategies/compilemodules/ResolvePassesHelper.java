package dev.nimbler.compiler.resolver.build.strategies.compilemodules;

import java.util.List;
import java.util.stream.Collectors;

import dev.nimbler.build.strategies.compilemodules.ParsedModule;
import dev.nimbler.compiler.model.common.CompilationUnitModel;
import dev.nimbler.compiler.resolver.ResolveError;
import dev.nimbler.compiler.resolver.build.CompilerOptions;
import dev.nimbler.compiler.resolver.passes.ParsedModuleAndCodeMap;
import dev.nimbler.compiler.resolver.passes.addfieldspass.AddFieldsPass;
import dev.nimbler.compiler.resolver.passes.addtokenrefspass.AddTokenRefsPass;
import dev.nimbler.compiler.resolver.passes.namereferenceresolve.NameReferenceResolvePass;
import dev.nimbler.compiler.resolver.passes.replacetyperefs.ReplaceTypeRefsPass;
import dev.nimbler.compiler.util.parse.ParsedFile;
import dev.nimbler.language.codemap.compiler.SynchronizedCompilerCodeMap;

public class ResolvePassesHelper {

    public static <COMPILATION_UNIT, PARSED_FILE extends ParsedFile>
    List<ResolveError> resolveParseTreeInPlaceFromCodeMap(
            ParsedModule<PARSED_FILE, ResolveError> module,
            CompilationUnitModel<COMPILATION_UNIT> compilationUnitModel,
            SynchronizedCompilerCodeMap codeMap,
            CompilerOptions options) {

        final ParsedModuleAndCodeMap<PARSED_FILE> parsedModuleAndCodeMap
            = new ParsedModuleAndCodeMap<>(module, codeMap);
        
        final ReplaceTypeRefsPass<PARSED_FILE, COMPILATION_UNIT> replaceTypeRefsPass
            = new ReplaceTypeRefsPass<>(compilationUnitModel);
        
        replaceTypeRefsPass.execute(parsedModuleAndCodeMap);
        
        final AddFieldsPass<PARSED_FILE, COMPILATION_UNIT> addFieldsPass
            = new AddFieldsPass<>(compilationUnitModel);
        
        addFieldsPass.execute(parsedModuleAndCodeMap);
        
        final NameReferenceResolvePass<PARSED_FILE, COMPILATION_UNIT> nameReferenceResolvePass
            = new NameReferenceResolvePass<>(compilationUnitModel);
        
        nameReferenceResolvePass.execute(parsedModuleAndCodeMap);
        
        if (options.isAddTokenRefsEnabled()) {
            final AddTokenRefsPass<PARSED_FILE, COMPILATION_UNIT> addTokenRefsPass
                = new AddTokenRefsPass<>(compilationUnitModel);
        
            addTokenRefsPass.execute(parsedModuleAndCodeMap);
        }
        
        return parsedModuleAndCodeMap.getParsed().stream()
                .flatMap(p -> p.getResolveErrorsList().stream())
                .collect(Collectors.toList());
    }
}
