package com.neaterbits.compiler.model.objects;

import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.model.test.BaseIterateTypesTest;

public class IterateTypesObjectsTest extends BaseIterateTypesTest<CompilationUnit> {

    public IterateTypesObjectsTest() {
        super(new ObjectsParseTreeTestModel());
    }
}
