package com.neaterbits.compiler.parser.listener.common;

import com.neaterbits.util.io.strings.Tokenizer;

public interface CreateParserListener<COMPILATION_UNIT> {

    IterativeParseTreeListener<COMPILATION_UNIT> create(String file, Tokenizer tokenizer);
    
}
