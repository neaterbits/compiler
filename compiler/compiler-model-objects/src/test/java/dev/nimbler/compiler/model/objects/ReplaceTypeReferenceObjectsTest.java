package dev.nimbler.compiler.model.objects;

import dev.nimbler.compiler.ast.objects.CompilationUnit;
import dev.nimbler.compiler.model.test.BaseReplaceTypeReferenceTest;

public class ReplaceTypeReferenceObjectsTest extends BaseReplaceTypeReferenceTest<CompilationUnit> {

    public ReplaceTypeReferenceObjectsTest() {
        super(new ObjectsParseTreeTestModel());
    }
}
