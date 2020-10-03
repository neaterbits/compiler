package com.neaterbits.compiler.model.encoded;

import com.neaterbits.compiler.ast.encoded.EncodedCompilationUnit;
import com.neaterbits.compiler.model.test.BaseIterateTypesTest;

public final class IterateTypesEncodedTest extends BaseIterateTypesTest<EncodedCompilationUnit> {

    public IterateTypesEncodedTest() {
        super(new EncodedParseTreeTestModel());
    }
}
