package com.neaterbits.compiler.model.objects;

import java.util.Collections;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.model.common.ParseTreeModel;
import com.neaterbits.compiler.model.test.BaseIterateTypesTest;
import com.neaterbits.compiler.model.test.TestTokenizer;
import com.neaterbits.compiler.parser.listener.common.ParseTreeListener;

public class IterateTypesObjectsTest extends BaseIterateTypesTest<CompilationUnit> {

    @SuppressWarnings("unchecked")
    @Override
    protected ParseTreeListener<CompilationUnit> makeParseTreeListener(String fileName, TestTokenizer testTokenizer) {

        return new ObjectsParserListener(testTokenizer, Collections.emptyList());
    }

    @Override
    protected ParseTreeModel<CompilationUnit> makeParseTreeModel() {

        return new ObjectProgramModel();
    }
}
