package com.neaterbits.compiler.parser.recursive;

import java.io.IOException;

import com.neaterbits.util.parse.ParserException;

@FunctionalInterface
public interface ProcessParts<T> {

    void processParts(T parts) throws IOException, ParserException;

}
