package com.neaterbits.compiler.java;

import java.util.function.Function;

import com.neaterbits.compiler.parser.listener.common.IterativeParseTreeListener;
import com.neaterbits.util.io.buffers.StringBuffers;

public interface CreateParserListener<COMPILATION_UNIT> 
    extends Function<StringBuffers, IterativeParseTreeListener<COMPILATION_UNIT>> {
    
}
