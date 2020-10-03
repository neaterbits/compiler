package com.neaterbits.compiler.model.objects;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.model.test.BaseReplaceTypeReferenceTest;

public class ReplaceTypeReferenceObjectsTest extends BaseReplaceTypeReferenceTest<CompilationUnit> {

    public ReplaceTypeReferenceObjectsTest() {
        super(new ObjectsParseTreeTestModel());
    }
}
