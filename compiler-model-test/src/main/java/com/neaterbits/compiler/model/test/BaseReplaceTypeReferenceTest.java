package com.neaterbits.compiler.model.test;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.neaterbits.compiler.model.common.TypeReferenceVisitor;

public abstract class BaseReplaceTypeReferenceTest<COMPILATION_UNIT>
    extends BaseParseTreeTest<COMPILATION_UNIT> {

    public BaseReplaceTypeReferenceTest(ParseTreeTestModel<COMPILATION_UNIT> testModel) {
        super(testModel);
    }

    @Test
    public void testReplaceReference() {

        final Util util = makeUtil();

        util.builder.startCompilationUnit();

        util.builder.addNonScopedTypeReference("ALanguageType");

        final COMPILATION_UNIT compilationUnit = util.builder.endCompilationUnit();

        final TypeReferenceVisitor visitor
            = Mockito.mock(TypeReferenceVisitor.class);

        util.parseTreeModel.iterateTypeReferences(compilationUnit, visitor);

        final ArgumentCaptor<Integer> parseTreeElementCaptor
            = ArgumentCaptor.forClass(Integer.class);

        Mockito.verify(visitor).onNonScopedTypeReference(
                parseTreeElementCaptor.capture(),
                ArgumentMatchers.eq("ALanguageType"));

        final int parseTreeElement = parseTreeElementCaptor.getValue();

        Mockito.verifyNoMoreInteractions(visitor);
        Mockito.reset(visitor);

        final int typeNo = 351;

        util.parseTreeModel.replaceTypeReference(
                compilationUnit,
                parseTreeElement,
                typeNo);

        util.parseTreeModel.iterateTypeReferences(compilationUnit, visitor);

        Mockito.verify(visitor).onResolvedTypeReference(
                ArgumentMatchers.anyInt(),
                ArgumentMatchers.eq(typeNo));

        Mockito.verifyNoMoreInteractions(visitor);
    }
}
