package dev.nimbler.compiler.parser.recursive.cached;

import java.io.IOException;

import org.jutils.parse.ParserException;

public interface ScratchList<TO_PROCESS> {

    void complete(ProcessParts<TO_PROCESS> process) throws IOException, ParserException;
}
