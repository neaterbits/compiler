package com.neaterbits.compiler.resolver.passes.addtokenrefspass;

import java.util.Objects;

import com.neaterbits.build.strategies.compilemodules.ParsedWithCachedRefs;
import com.neaterbits.compiler.model.common.CompilationUnitModel;
import com.neaterbits.compiler.model.common.passes.MultiPass;
import com.neaterbits.compiler.resolver.ResolveError;
import com.neaterbits.compiler.resolver.passes.ParsedModuleAndCodeMap;
import com.neaterbits.compiler.util.parse.ParsedFile;

public final class AddTokenRefsPass<PARSED_FILE extends ParsedFile, COMPILATION_UNIT>
        extends MultiPass<ParsedModuleAndCodeMap<PARSED_FILE>, ParsedModuleAndCodeMap<PARSED_FILE>> {

    private final CompilationUnitModel<COMPILATION_UNIT> compilationUnitModel;

    public AddTokenRefsPass(CompilationUnitModel<COMPILATION_UNIT> compilationUnitModel) {

        Objects.requireNonNull(compilationUnitModel);
        
        this.compilationUnitModel = compilationUnitModel;
    }

    @Override
    public ParsedModuleAndCodeMap<PARSED_FILE> execute(ParsedModuleAndCodeMap<PARSED_FILE> input) {

        
        for (ParsedWithCachedRefs<PARSED_FILE, ResolveError> parsed : input.getParsed()) {
            final AddTokenRefsVisitor<COMPILATION_UNIT> visitor
                = new AddTokenRefsVisitor<>(input.getCodeMap(), parsed.getCodeMapFileNo());

            compilationUnitModel.iterateResolvedScopesAndVariables(
                    parsed.getParsedFile().getCompilationUnit(),
                    visitor);
        }

        return input;
    }
}
