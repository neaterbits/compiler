package com.neaterbits.compiler.common.antlr4;

import com.neaterbits.compiler.common.Parser;

public interface AntlrParser<T, LISTENER extends ModelParserListener<T>> extends Parser<T, AntlrError, LISTENER> {

}
