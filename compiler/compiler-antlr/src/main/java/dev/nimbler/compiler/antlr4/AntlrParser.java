package dev.nimbler.compiler.antlr4;

import dev.nimbler.compiler.util.parse.ListenerParser;
import dev.nimbler.compiler.util.parse.Parser;

public interface AntlrParser<T, LISTENER extends ModelParserListener<T>> extends Parser<T>, ListenerParser<LISTENER> {

}
