package com.neaterbits.compiler.model.objects;

import java.util.Collections;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.model.common.ParseTreeModel;
import com.neaterbits.compiler.model.test.ParseTreeTestModel;
import com.neaterbits.compiler.model.test.TestTokenizer;
import com.neaterbits.compiler.parser.listener.common.ParseTreeListener;

final class ObjectsParseTreeTestModel extends ParseTreeTestModel<CompilationUnit> {

    @SuppressWarnings("unchecked")
    @Override
    protected ParseTreeListener<CompilationUnit> makeParseTreeListener(String fileName, TestTokenizer testTokenizer) {

        return new ObjectsParserListener(fileName, testTokenizer, Collections.emptyList(), null);
    }

    @Override
    protected ParseTreeModel<CompilationUnit> makeParseTreeModel() {

        return new ObjectProgramModel();
    }
}
