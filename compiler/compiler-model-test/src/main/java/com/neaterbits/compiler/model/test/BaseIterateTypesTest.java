package com.neaterbits.compiler.model.test;

import org.junit.Test;
import org.mockito.Mockito;

import com.neaterbits.compiler.model.common.TypeVisitor;

public abstract class BaseIterateTypesTest<COMPILATION_UNIT> extends BaseParseTreeTest<COMPILATION_UNIT> {

    public BaseIterateTypesTest(ParseTreeTestModel<COMPILATION_UNIT> testModel) {
        super(testModel);
    }

    @Test
    public void testIterateTypes() {

        final Util util = makeUtil();

        util.builder.startCompilationUnit();

        util.builder.startNamespace();

        util.builder.addNamespacePart("namespace");

        util.builder.startClass("TestClass");

        util.builder.endClass();

        util.builder.startInterface("TestInterface");

        util.builder.endInterface();

        util.builder.startEnum("TestEnum");

        util.builder.endEnum();

        util.builder.endNamespace();

        final COMPILATION_UNIT compilationUnit = util.builder.endCompilationUnit();

        final TypeVisitor typeVisitor = Mockito.mock(TypeVisitor.class);
        Mockito.inOrder(typeVisitor);

        util.parseTreeModel.iterateTypes(compilationUnit, typeVisitor);

        Mockito.verify(typeVisitor).onNamespaceStart();

        Mockito.verify(typeVisitor).onNamespacePart("namespace");

        Mockito.verify(typeVisitor).onClassStart("TestClass");

        Mockito.verify(typeVisitor).onClassEnd();

        Mockito.verify(typeVisitor).onInterfaceStart("TestInterface");

        Mockito.verify(typeVisitor).onInterfaceEnd();

        Mockito.verify(typeVisitor).onEnumStart("TestEnum");

        Mockito.verify(typeVisitor).onEnumEnd();

        Mockito.verify(typeVisitor).onNamespaceEnd();

        Mockito.verifyNoMoreInteractions(typeVisitor);
    }
}
