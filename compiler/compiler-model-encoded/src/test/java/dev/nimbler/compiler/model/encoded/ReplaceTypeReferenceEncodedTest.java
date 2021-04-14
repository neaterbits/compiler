package dev.nimbler.compiler.model.encoded;

import dev.nimbler.compiler.ast.encoded.EncodedCompilationUnit;
import dev.nimbler.compiler.model.test.BaseReplaceTypeReferenceTest;

public class ReplaceTypeReferenceEncodedTest
    extends BaseReplaceTypeReferenceTest<EncodedCompilationUnit> {

    public ReplaceTypeReferenceEncodedTest() {
        super(new EncodedParseTreeTestModel());
    }
}
