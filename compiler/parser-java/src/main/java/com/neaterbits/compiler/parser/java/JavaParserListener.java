package com.neaterbits.compiler.parser.java;

import com.neaterbits.compiler.util.parse.listener.binary.BaseIterativeParserListener;
import com.neaterbits.util.io.strings.Tokenizer;

public class JavaParserListener<COMPILATION_UNIT>
    extends BaseIterativeParserListener<COMPILATION_UNIT> {

    public JavaParserListener(Tokenizer tokenizer) {
        super(tokenizer);
    }
}
