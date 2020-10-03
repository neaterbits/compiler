package com.neaterbits.compiler.model.encoded;

import java.util.Collections;

import com.neaterbits.compiler.ast.encoded.EncodedCompilationUnit;
import com.neaterbits.compiler.model.common.ParseTreeModel;
import com.neaterbits.compiler.model.test.ParseTreeTestModel;
import com.neaterbits.compiler.model.test.TestTokenizer;
import com.neaterbits.compiler.parser.listener.common.ParseTreeListener;

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
