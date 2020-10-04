package com.neaterbits.compiler.resolver.passes.replacetyperefs;

import java.io.IOException;
import java.util.Objects;

import com.neaterbits.compiler.model.common.CompilationUnitModel;
import com.neaterbits.compiler.model.common.TypeReferenceVisitor;
import com.neaterbits.compiler.model.common.passes.MultiPass;
import com.neaterbits.compiler.resolver.passes.typefinder.FoundTypeFiles;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.util.parse.ParserException;

public final class ReplaceTypeRefsPass<PARSED_FILE extends ParsedFile, COMPILATION_UNIT>
    extends MultiPass<FoundTypeFiles<PARSED_FILE>, FoundTypeFiles<PARSED_FILE>> {

    private final CompilationUnitModel<COMPILATION_UNIT> compilationUnitModel;

    public ReplaceTypeRefsPass(CompilationUnitModel<COMPILATION_UNIT> compilationUnitModel) {

        Objects.requireNonNull(compilationUnitModel);

        this.compilationUnitModel = compilationUnitModel;
    }

    @Override
    public FoundTypeFiles<PARSED_FILE> execute(FoundTypeFiles<PARSED_FILE> input)
            throws IOException, ParserException {

        // Scan through all references and replace them
        final TypeReferenceVisitor<COMPILATION_UNIT> visitor
            = new ReplaceTypeReferenceVisitor<>(compilationUnitModel, input.getCodeMap(), input);

        for (PARSED_FILE parsedFile : input.getParsedFiles()) {

            final COMPILATION_UNIT compilationUnit = parsedFile.getCompilationUnit();

            compilationUnitModel.iterateTypeReferences(compilationUnit, visitor);
        }

        return input;
    }
}
