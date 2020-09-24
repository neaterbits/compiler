package com.neaterbits.compiler.resolver.passes.typefinder;

import java.io.IOException;
import java.util.Objects;

import com.neaterbits.compiler.model.common.ParseTreeModel;
import com.neaterbits.compiler.model.common.passes.MultiPass;
import com.neaterbits.compiler.resolver.passes.ParsedFilesAndCodeMap;
import com.neaterbits.compiler.util.parse.ParsedFile;
import com.neaterbits.util.parse.ParserException;

public final class TypeFinderPass<PARSED_FILE extends ParsedFile, COMPILATION_UNIT>
    extends MultiPass<ParsedFilesAndCodeMap<PARSED_FILE>, FoundTypeFiles<PARSED_FILE>> {

    private final ParseTreeModel<COMPILATION_UNIT> parseTreeModel;

    public TypeFinderPass(ParseTreeModel<COMPILATION_UNIT> parseTreeModel) {

        Objects.requireNonNull(parseTreeModel);

        this.parseTreeModel = parseTreeModel;
    }

    @Override
    public FoundTypeFiles<PARSED_FILE> execute(ParsedFilesAndCodeMap<PARSED_FILE> input)
            throws IOException, ParserException {

        final TypeFinderVisitor typeFinderVisitor = new TypeFinderVisitor(input.getCodeMap());

        for (PARSED_FILE file : input.getParsedFiles()) {
            parseTreeModel.iterateTypes(file.getCompilationUnit(), typeFinderVisitor);
        }

        return new FoundTypeFiles<>(input);
    }
}
