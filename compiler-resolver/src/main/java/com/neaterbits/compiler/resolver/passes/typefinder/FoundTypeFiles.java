package com.neaterbits.compiler.resolver.passes.typefinder;

import java.util.Map;
import java.util.Objects;

import com.neaterbits.compiler.model.common.TypesMap;
import com.neaterbits.compiler.resolver.passes.ParsedFilesAndCodeMap;
import com.neaterbits.compiler.util.ScopedName;
import com.neaterbits.compiler.util.TypeName;
import com.neaterbits.compiler.util.parse.ParsedFile;

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
