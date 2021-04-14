package dev.nimbler.compiler.model.objects;

import java.util.Collections;

import dev.nimbler.compiler.ast.objects.CompilationUnit;
import dev.nimbler.compiler.model.common.ParseTreeModel;
import dev.nimbler.compiler.model.test.ParseTreeTestModel;
import dev.nimbler.compiler.model.test.TestTokenizer;
import dev.nimbler.compiler.parser.listener.common.ParseTreeListener;

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
