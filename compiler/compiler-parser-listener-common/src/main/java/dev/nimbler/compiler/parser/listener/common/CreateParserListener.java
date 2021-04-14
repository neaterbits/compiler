package dev.nimbler.compiler.parser.listener.common;

import com.neaterbits.util.io.strings.Tokenizer;

public interface CreateParserListener<COMPILATION_UNIT> {

    IterativeParseTreeListener<COMPILATION_UNIT> createParserListener(String file, Tokenizer tokenizer);
    
}
