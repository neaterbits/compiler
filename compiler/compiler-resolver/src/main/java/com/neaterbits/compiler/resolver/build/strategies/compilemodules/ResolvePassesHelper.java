package com.neaterbits.compiler.resolver.build.strategies.compilemodules;

import java.util.List;
import java.util.stream.Collectors;

import com.neaterbits.build.strategies.compilemodules.ParsedModule;
import com.neaterbits.compiler.model.common.CompilationUnitModel;
import com.neaterbits.compiler.resolver.ResolveError;
import com.neaterbits.compiler.resolver.build.CompilerOptions;
import com.neaterbits.compiler.resolver.passes.ParsedModuleAndCodeMap;
import com.neaterbits.compiler.resolver.passes.addfieldspass.AddFieldsPass;
import com.neaterbits.compiler.resolver.passes.addtokenrefspass.AddTokenRefsPass;
import com.neaterbits.compiler.resolver.passes.namereferenceresolve.NameReferenceResolvePass;
import com.neaterbits.compiler.resolver.passes.replacetyperefs.ReplaceTypeRefsPass;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.language.codemap.compiler.SynchronizedCompilerCodeMap;

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
