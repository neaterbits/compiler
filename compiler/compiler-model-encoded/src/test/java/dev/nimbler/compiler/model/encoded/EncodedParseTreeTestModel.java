package dev.nimbler.compiler.model.encoded;

import java.util.Collections;

import dev.nimbler.compiler.ast.encoded.EncodedCompilationUnit;
import dev.nimbler.compiler.model.common.ParseTreeModel;
import dev.nimbler.compiler.model.test.ParseTreeTestModel;
import dev.nimbler.compiler.model.test.TestTokenizer;
import dev.nimbler.compiler.parser.listener.common.ParseTreeListener;

final class EncodedParseTreeTestModel extends ParseTreeTestModel<EncodedCompilationUnit> {

    @Override
    protected ParseTreeListener<EncodedCompilationUnit>
        makeParseTreeListener(String fileName, TestTokenizer testTokenizer) {

        return new EncodedParserListener(fileName, testTokenizer);
    }

    @Override
    protected ParseTreeModel<EncodedCompilationUnit> makeParseTreeModel() {

        return new EncodedProgramModel(Collections.emptyList());
    }
}
