package com.neaterbits.compiler.antlr4;

import com.neaterbits.compiler.util.parse.Parser;

public interface AntlrParser<T, LISTENER extends ModelParserListener<T>> extends Parser<T, LISTENER> {

}
