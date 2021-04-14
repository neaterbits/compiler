package dev.nimbler.compiler.model.encoded;

import dev.nimbler.compiler.ast.encoded.EncodedCompilationUnit;
import dev.nimbler.compiler.model.test.BaseIterateTypesTest;

public final class IterateTypesEncodedTest extends BaseIterateTypesTest<EncodedCompilationUnit> {

    public IterateTypesEncodedTest() {
        super(new EncodedParseTreeTestModel());
    }
}
