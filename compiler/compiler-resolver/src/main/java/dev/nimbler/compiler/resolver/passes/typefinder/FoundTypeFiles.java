package dev.nimbler.compiler.resolver.passes.typefinder;

import java.util.Map;
import java.util.Objects;

import dev.nimbler.compiler.resolver.passes.ParsedFilesAndCodeMap;
import dev.nimbler.compiler.util.parse.ParsedFile;
import dev.nimbler.language.common.types.ScopedName;
import dev.nimbler.language.common.types.TypeName;
import dev.nimbler.language.common.types.TypesMap;

public final class FoundTypeFiles<PARSED_FILE extends ParsedFile>
    extends ParsedFilesAndCodeMap<PARSED_FILE>
    implements TypesMap<TypeName> {

    private final Map<ScopedName, TypeName> typeNameByScopedName;

    public FoundTypeFiles(
            ParsedFilesAndCodeMap<PARSED_FILE> parsedFilesAndCodeMap,
            Map<ScopedName, TypeName> typeNameByScopedName) {
        super(parsedFilesAndCodeMap);

        this.typeNameByScopedName = typeNameByScopedName;
    }

    @Override
    public TypeName lookupByScopedName(ScopedName scopedName) {

        Objects.requireNonNull(scopedName);

        return typeNameByScopedName.get(scopedName);
    }
}
