package com.neaterbits.compiler.resolver.passes.replacetyperefs;

import java.util.Objects;

import com.neaterbits.build.strategies.compilemodules.ParsedWithCachedRefs;
import com.neaterbits.compiler.model.common.CompilationUnitModel;
import com.neaterbits.compiler.model.common.TypeReferenceVisitor;
import com.neaterbits.compiler.model.common.passes.MultiPass;
import com.neaterbits.compiler.resolver.ResolveError;
import com.neaterbits.compiler.resolver.passes.ParsedModuleAndCodeMap;
import com.neaterbits.compiler.util.parse.ParsedFile;

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
