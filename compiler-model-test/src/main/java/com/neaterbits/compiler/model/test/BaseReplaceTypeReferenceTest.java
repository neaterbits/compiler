package com.neaterbits.compiler.model.test;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.neaterbits.build.types.TypeName;
import com.neaterbits.compiler.model.common.TypeReferenceVisitor;

public abstract class BaseReplaceTypeReferenceTest<COMPILATION_UNIT>
    extends BaseParseTreeTest<COMPILATION_UNIT> {

    public BaseReplaceTypeReferenceTest(ParseTreeTestModel<COMPILATION_UNIT> testModel) {
        super(testModel);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReplaceReference() {

        final Util util = makeUtil();

        util.builder.startCompilationUnit();

        util.builder.startNamespace();

        util.builder.addNamespacePart("namespace");

        util.builder.startClass("TestClass");

        util.builder.startClassExtends();

        util.builder.addNonScopedTypeReference("ALanguageType");

        util.builder.endClassExtends();

        util.builder.endClass();

        util.builder.endNamespace();

        final COMPILATION_UNIT compilationUnit = util.builder.endCompilationUnit();

        final TypeReferenceVisitor<COMPILATION_UNIT> visitor
            = Mockito.mock(TypeReferenceVisitor.class);

        util.parseTreeModel.iterateTypeReferences(compilationUnit, visitor);

        final ArgumentCaptor<Integer> parseTreeElementCaptor
            = ArgumentCaptor.forClass(Integer.class);

        Mockito.verify(visitor).onNonScopedTypeReference(
                ArgumentMatchers.same(compilationUnit),
                parseTreeElementCaptor.capture(),
                ArgumentMatchers.eq("ALanguageType"));

        final int parseTreeElement = parseTreeElementCaptor.getValue();

        Mockito.verifyNoMoreInteractions(visitor);
        Mockito.reset(visitor);

        final int typeNo = 351;
        final TypeName typeName = new TypeName(null, null, "ALanguageType");

        util.parseTreeModel.replaceTypeReference(
                compilationUnit,
                parseTreeElement,
                typeNo,
                typeName);

        util.parseTreeModel.iterateTypeReferences(compilationUnit, visitor);

        Mockito.verify(visitor).onResolvedTypeReference(
                ArgumentMatchers.same(compilationUnit),
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.eq(typeNo));

        Mockito.verifyNoMoreInteractions(visitor);
    }
}
