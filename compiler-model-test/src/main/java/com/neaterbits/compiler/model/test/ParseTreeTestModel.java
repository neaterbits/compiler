package com.neaterbits.compiler.model.test;

import com.neaterbits.compiler.model.common.ParseTreeModel;
import com.neaterbits.compiler.parser.listener.common.ParseTreeListener;

public abstract class ParseTreeTestModel<COMPILATION_UNIT> {

    protected abstract ParseTreeListener<COMPILATION_UNIT>
        makeParseTreeListener(String fileName, TestTokenizer testTokenizer);

    protected abstract ParseTreeModel<COMPILATION_UNIT> makeParseTreeModel();
}
