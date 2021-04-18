package dev.nimbler.compiler.resolver.passes.typefinder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.jutils.parse.ParserException;

import dev.nimbler.compiler.model.common.ParseTreeModel;
import dev.nimbler.compiler.model.common.passes.MultiPass;
import dev.nimbler.compiler.resolver.passes.ParsedFilesAndCodeMap;
import dev.nimbler.compiler.util.parse.ParsedFile;
import dev.nimbler.language.common.types.ScopedName;
import dev.nimbler.language.common.types.TypeName;

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

        final Map<ScopedName, TypeName> typeNameByScopedName = new HashMap<>();

        final TypeFinderVisitor typeFinderVisitor
            = new TypeFinderVisitor(input.getCodeMap(), typeNameByScopedName);

        for (PARSED_FILE file : input.getParsedFiles()) {
            parseTreeModel.iterateTypes(file.getCompilationUnit(), typeFinderVisitor);
        }

        return new FoundTypeFiles<>(input, typeNameByScopedName);
    }
}
