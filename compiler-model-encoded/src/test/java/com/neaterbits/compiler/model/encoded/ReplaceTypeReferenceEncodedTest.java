package com.neaterbits.compiler.model.encoded;

import com.neaterbits.compiler.ast.encoded.EncodedCompilationUnit;
import com.neaterbits.compiler.model.test.BaseReplaceTypeReferenceTest;

public class ReplaceTypeReferenceEncodedTest
    extends BaseReplaceTypeReferenceTest<EncodedCompilationUnit> {

    public ReplaceTypeReferenceEncodedTest() {
        super(new EncodedParseTreeTestModel());
    }
}
