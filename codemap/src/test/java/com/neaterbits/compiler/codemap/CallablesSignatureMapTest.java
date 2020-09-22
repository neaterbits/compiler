package com.neaterbits.compiler.codemap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

public class CallablesSignatureMapTest {

    @Test
    public void testAddSignature() {

        final int paramType1 = 2;
        final int paramType2 = 3;

        final CallablesSignatureMap signatureMap = new CallablesSignatureMap();

        final int signatureNo = signatureMap.findOrAddSignature(
                "someMethod",
                new int [] { paramType1, paramType2 });

        assertThat(signatureNo).isEqualTo(0);

        final int anotherSignatureNo = signatureMap.findOrAddSignature(
                "someOtherMethod",
                new int [] { paramType1 });

        assertThat(anotherSignatureNo).isEqualTo(1);

        final int overrideSignatureNo = signatureMap.findOrAddSignature(
                "someMethod",
                new int [] { paramType1, paramType2 });

        // Same signature
        assertThat(overrideSignatureNo).isEqualTo(0);

        assertThat(signatureMap.getCallableName(signatureMap.getCallableNameNo("someMethod")))
            .isEqualTo("someMethod");

        assertThat(signatureMap.getCallableName(signatureMap.getCallableNameNo("someOtherMethod")))
            .isEqualTo("someOtherMethod");

        assertThat(signatureMap.getCallableSignatureNo(
                signatureMap.getCallableNameNo("someMethod"),
                signatureMap.getParamTypesNo(new int [] { paramType1, paramType2 })))
            .isEqualTo(0);

        assertThat(signatureMap.getCallableNameNo("notSomeMethod")).isNull();

        assertThat(signatureMap.getCallableSignatureNo(
                signatureMap.getCallableNameNo("someMethod"),
                signatureMap.getParamTypesNo(new int [] { paramType1 })))
            .isEqualTo(-1);

        assertThat(signatureMap.getCallableSignatureNo(
                signatureMap.getCallableNameNo("someOtherMethod"),
                signatureMap.getParamTypesNo(new int [] { paramType1 })))
            .isEqualTo(1);

        assertThat(signatureMap.getSignatureParameterTypes(signatureNo))
            .isEqualTo(new int [] { paramType1, paramType2 });

        assertThat(signatureMap.getSignatureParameterTypes(anotherSignatureNo))
            .isEqualTo(new int [] { paramType1 });
    }

    @Test
    public void testGetParameterTypesForUnknownSignature() {

        final CallablesSignatureMap signatureMap = new CallablesSignatureMap();

        try {
            signatureMap.getSignatureParameterTypes(0);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {

        }
    }

    @Test
    public void testAddSignatureWithNoParameters() {

        final CallablesSignatureMap signatureMap = new CallablesSignatureMap();

        final int signatureNo = signatureMap.findOrAddSignature(
                "someMethod",
                new int [0]);

        assertThat(signatureNo).isEqualTo(0);

        assertThat(signatureMap.getSignatureParameterTypes(signatureNo))
            .isEqualTo(new int[0]);
    }
}
