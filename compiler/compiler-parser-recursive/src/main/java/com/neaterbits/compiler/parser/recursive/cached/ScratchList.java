package com.neaterbits.compiler.parser.recursive.cached;

import java.io.IOException;

import com.neaterbits.util.parse.ParserException;

public interface ScratchList<TO_PROCESS> {

    void complete(ProcessParts<TO_PROCESS> process) throws IOException, ParserException;
}
