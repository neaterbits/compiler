package dev.nimbler.compiler.model.objects;

import dev.nimbler.compiler.ast.objects.CompilationUnit;
import dev.nimbler.compiler.model.test.BaseIterateTypesTest;

public class IterateTypesObjectsTest extends BaseIterateTypesTest<CompilationUnit> {

    public IterateTypesObjectsTest() {
        super(new ObjectsParseTreeTestModel());
    }
}
