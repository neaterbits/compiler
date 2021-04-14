package dev.nimbler.compiler.resolver.passes.addtokenrefspass;

import java.util.Objects;

import dev.nimbler.build.strategies.compilemodules.ParsedWithCachedRefs;
import dev.nimbler.compiler.model.common.CompilationUnitModel;
import dev.nimbler.compiler.model.common.passes.MultiPass;
import dev.nimbler.compiler.resolver.ResolveError;
import dev.nimbler.compiler.resolver.passes.ParsedModuleAndCodeMap;
import dev.nimbler.compiler.util.parse.ParsedFile;

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
