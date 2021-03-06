package dev.nimbler.compiler.parser.recursive.cached;

import java.io.IOException;

import org.jutils.parse.ParserException;

@FunctionalInterface
public interface ProcessParts<T> {

    void processParts(T parts) throws IOException, ParserException;

}
