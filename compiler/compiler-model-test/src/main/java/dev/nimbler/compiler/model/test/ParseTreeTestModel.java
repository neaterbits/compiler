package dev.nimbler.compiler.model.test;

import dev.nimbler.compiler.model.common.ParseTreeModel;
import dev.nimbler.compiler.parser.listener.common.ParseTreeListener;

public abstract class ParseTreeTestModel<COMPILATION_UNIT> {

    protected abstract ParseTreeListener<COMPILATION_UNIT>
        makeParseTreeListener(String fileName, TestTokenizer testTokenizer);

    protected abstract ParseTreeModel<COMPILATION_UNIT> makeParseTreeModel();
}
