package com.neaterbits.compiler.resolver.passes.typefinder;

import com.neaterbits.compiler.resolver.passes.ParsedFilesAndCodeMap;
import com.neaterbits.compiler.util.parse.ParsedFile;

public final class FoundTypeFiles<PARSED_FILE extends ParsedFile>
    extends ParsedFilesAndCodeMap<PARSED_FILE> {

    public FoundTypeFiles(ParsedFilesAndCodeMap<PARSED_FILE> parsedFilesAndCodeMap) {
        super(parsedFilesAndCodeMap);
    }
}
