package com.neaterbits.compiler.model.test;

import com.neaterbits.compiler.model.common.ParseTreeModel;
import com.neaterbits.compiler.parser.listener.common.ParseTreeListener;

public abstract class BaseParseTreeTest<COMPILATION_UNIT> {

    private final ParseTreeTestModel<COMPILATION_UNIT> testModel;

    static class Util<COMPILATION_UNIT> {
        final ParseTreeBuilder<COMPILATION_UNIT> builder;
        final ParseTreeModel<COMPILATION_UNIT> parseTreeModel;

        Util(ParseTreeBuilder<COMPILATION_UNIT> builder, ParseTreeModel<COMPILATION_UNIT> parseTreeModel) {
            this.builder = builder;
            this.parseTreeModel = parseTreeModel;
        }
    }

    BaseParseTreeTest(ParseTreeTestModel<COMPILATION_UNIT> testModel) {
        this.testModel = testModel;
    }

    final Util<COMPILATION_UNIT> makeUtil() {

        final TestTokenizer testTokenizer = new TestTokenizer();

        final ParseTreeListener<COMPILATION_UNIT> parserListener
            = testModel.makeParseTreeListener("file", testTokenizer);

        final ParseTreeBuilder<COMPILATION_UNIT> builder
            = new ParseTreeBuilder<>(parserListener, testTokenizer);

        final ParseTreeModel<COMPILATION_UNIT> parseTreeModel = testModel.makeParseTreeModel();

        return new Util<>(builder, parseTreeModel);
    }
}
