package dev.nimbler.compiler.resolver.passes.replacetyperefs;

import java.util.Objects;

import dev.nimbler.build.strategies.compilemodules.ParsedWithCachedRefs;
import dev.nimbler.compiler.model.common.CompilationUnitModel;
import dev.nimbler.compiler.model.common.TypeReferenceVisitor;
import dev.nimbler.compiler.model.common.passes.MultiPass;
import dev.nimbler.compiler.resolver.ResolveError;
import dev.nimbler.compiler.resolver.passes.ParsedModuleAndCodeMap;
import dev.nimbler.compiler.util.parse.ParsedFile;

public final class ReplaceTypeRefsPass<PARSED_FILE extends ParsedFile, COMPILATION_UNIT>
    extends MultiPass<ParsedModuleAndCodeMap<PARSED_FILE>, ParsedModuleAndCodeMap<PARSED_FILE>> {

    private final CompilationUnitModel<COMPILATION_UNIT> compilationUnitModel;

    public ReplaceTypeRefsPass(CompilationUnitModel<COMPILATION_UNIT> compilationUnitModel) {

        Objects.requireNonNull(compilationUnitModel);

        this.compilationUnitModel = compilationUnitModel;
    }

    @Override
    public ParsedModuleAndCodeMap<PARSED_FILE> execute(ParsedModuleAndCodeMap<PARSED_FILE> input) {

        // Scan through all references and replace them

        for (ParsedWithCachedRefs<PARSED_FILE, ResolveError> parsed : input.getParsed()) {

            final TypeReferenceVisitor<COMPILATION_UNIT> visitor
                = new ReplaceTypeReferenceVisitor<>(
                        compilationUnitModel,
                        input.getCodeMap(),
                        input.getTypesMap(),
                        parsed.getResolveErrorsList());

            final COMPILATION_UNIT compilationUnit = parsed.getParsedFile().getCompilationUnit();

            compilationUnitModel.iterateTypeReferences(compilationUnit, visitor);
        }

        return input;
    }
}
