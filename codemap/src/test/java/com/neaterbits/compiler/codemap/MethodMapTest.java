package com.neaterbits.compiler.codemap;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.compiler.codemap.CodeMap.MethodFilter;
import com.neaterbits.compiler.codemap.MethodMap.GetSuperType;
import com.neaterbits.compiler.types.MethodInfo;
import com.neaterbits.compiler.types.MethodVariant;

public class MethodMapTest {

	@Test
	public void testAddMethod() {

		final int typeNo = 1;

		final int paramType1 = 2;
		final int paramType2 = 3;

		final MethodMap methodMap = new MethodMap();

		final StaticMethodOverrideMap methodOverrideMap = new StaticMethodOverrideMap();

		methodMap.allocateMethods(typeNo, 3);

		final int methodNo = methodMap.addMethod(
				typeNo,
				TypeVariant.CLASS,
				"someMethod",
				new int [] { paramType1, paramType2 },
				MethodVariant.OVERRIDABLE_IMPLEMENTATION,
				0);

		assertThat(methodNo).isEqualTo(0);

		assertThat(methodMap.getTypeForMethod(methodNo)).isEqualTo(typeNo);

		final int anotherMethodNo = methodMap.addMethod(
				typeNo,
				TypeVariant.CLASS,
				"someOtherMethod",
				new int [] { paramType1 },
				MethodVariant.FINAL_IMPLEMENTATION,
				1);

		assertThat(anotherMethodNo).isEqualTo(1);

		final int anotherTypeNo = 4;

		methodMap.allocateMethods(anotherTypeNo, 1);

		final int overrideMethodNo = methodMap.addMethod(
				anotherTypeNo,
				TypeVariant.CLASS,
				"someMethod",
				new int [] { paramType1, paramType2 },
				MethodVariant.FINAL_IMPLEMENTATION,
				0);

		assertThat(overrideMethodNo).isEqualTo(2);

		assertThat(methodMap.getMethodNo(1, "someMethod", new int [] { paramType1, paramType2 })).isEqualTo(0);
		assertThat(methodMap.getMethodNo(1, "notSomeMethod", new int [] { paramType1, paramType2 })).isEqualTo(-1);

		assertThat(methodMap.getMethodNo(1, "someMethod", new int [] { paramType1 })).isEqualTo(-1);
		assertThat(methodMap.getMethodNo(1, "someOtherMethod", new int [] { paramType1 })).isEqualTo(1);
		assertThat(methodMap.getMethodNo(4, "someMethod", new int [] { paramType1, paramType2 })).isEqualTo(2);

		assertThat(methodMap.getMethodName(methodNo)).isEqualTo("someMethod");
		assertThat(methodMap.getMethodName(anotherMethodNo)).isEqualTo("someOtherMethod");
		assertThat(methodMap.getMethodName(overrideMethodNo)).isEqualTo("someMethod");

		assertThat(methodMap.getMethodParameterTypes(methodNo)).containsExactly(paramType1, paramType2);
		assertThat(methodMap.getMethodParameterTypes(anotherMethodNo)).containsExactly(paramType1);
		assertThat(methodMap.getMethodParameterTypes(overrideMethodNo)).containsExactly(paramType1, paramType2);

		methodOverrideMap.addTypeExtendsTypes(
				Encode.encodeType(anotherTypeNo, TypeVariant.CLASS),
				type -> type == anotherTypeNo
				    ? new int [] { Encode.encodeType(typeNo, TypeVariant.CLASS) }
				    : null,
				methodMap);

		assertThat(methodOverrideMap.getNumberOfMethodsDirectlyExtending(methodNo)).isEqualTo(1);
		assertThat(methodOverrideMap.getMethodsDirectlyExtending(methodNo).length).isEqualTo(1);

		assertThat(Encode.decodeMethodNo(methodOverrideMap.getMethodsDirectlyExtending(methodNo)[0])).isEqualTo(overrideMethodNo);

		assertThat(methodOverrideMap.getMethodsDirectlyExtending(methodNo)[0])
		    .isEqualTo(overrideMethodNo);

		assertThat(methodOverrideMap.getNumberOfMethodsDirectlyExtendedBy(overrideMethodNo)).isEqualTo(1);
		assertThat(methodOverrideMap.getMethodsDirectlyExtendedBy(overrideMethodNo).length).isEqualTo(1);
		assertThat(methodOverrideMap.getMethodsDirectlyExtendedBy(overrideMethodNo)[0])
		    .isEqualTo(methodNo);
	}

	@Test
	public void testGetMethodInfoForNoParamsMethod() {

        final int typeNo = 1;

        final MethodMap methodMap = new MethodMap();

        methodMap.allocateMethods(typeNo, 3);

        final int methodNo = methodMap.addMethod(
                typeNo,
                TypeVariant.CLASS,
                "someMethod",
                new int [0],
                MethodVariant.OVERRIDABLE_IMPLEMENTATION,
                0);

        final MethodInfo methodInfo = methodMap.getMethodInfo(typeNo, "someMethod", new int[0]);

        assertThat(methodInfo).isNotNull();

        assertThat(methodInfo.getMethodNo()).isEqualTo(methodNo);
	}

	@Test
    public void testGetMethodInfoForUnknownParams() {

        final int typeNo = 1;

        final int paramType1 = 2;
        final int paramType2 = 3;
        final int paramType3 = 4;

        final MethodMap methodMap = new MethodMap();

        methodMap.allocateMethods(typeNo, 3);

        final int methodNo = methodMap.addMethod(
                typeNo,
                TypeVariant.CLASS,
                "someMethod",
                new int [] { paramType1, paramType2 },
                MethodVariant.OVERRIDABLE_IMPLEMENTATION,
                0);

        assertThat(methodNo).isGreaterThanOrEqualTo(0);

        assertThat(methodMap.getMethodInfo(
                typeNo,
                "someMethod",
                new int [] { paramType1, paramType3 }))
            .isNull();
    }

    @Test
    public void testGetMethodNoForUnknownSignature() {

        final int typeNo = 1;

        final int paramType1 = 2;
        final int paramType2 = 3;

        final MethodMap methodMap = new MethodMap();

        methodMap.allocateMethods(typeNo, 3);

        methodMap.addMethod(
                typeNo,
                TypeVariant.CLASS,
                "someMethod",
                new int [] { paramType1, paramType2 },
                MethodVariant.OVERRIDABLE_IMPLEMENTATION,
                0);

        assertThat(methodMap.getMethodNoBySignatureNo(typeNo, 123)).isEqualTo(-1);
    }

    @Test
    public void testGetDistinctMethods() {

        final MethodMap methodMap = new MethodMap();

        final int typeNo = 1;

        final int numMethods = 100;

        methodMap.allocateMethods(typeNo, numMethods);

        for (int i = 0; i < numMethods; ++ i) {

            methodMap.addMethod(
                    typeNo,
                    TypeVariant.CLASS,
                    "method" + i,
                    new int [0],
                    MethodVariant.OVERRIDABLE_IMPLEMENTATION,
                    i);
        }

        final VTableScratchArea scratchArea = new VTableScratchArea();

        final MethodFilter methodFilter = (methodNo, variant) -> true;

        final GetSuperType getSuperType = type -> -1;

        assertThat(methodMap.getDistinctMethodCount(
                typeNo,
                methodFilter,
                getSuperType,
                scratchArea)).isEqualTo(numMethods);

        final int [] vtable = scratchArea.copyVTable();
        assertThat(vtable.length).isEqualTo(numMethods);
    }

    @Test
    public void testGetDistinctMethodsWithFilter() {

        final MethodMap methodMap = new MethodMap();

        final int typeNo = 1;

        final int numMethods = 100;

        methodMap.allocateMethods(typeNo, numMethods);

        for (int i = 0; i < numMethods; ++ i) {

            methodMap.addMethod(
                    typeNo,
                    TypeVariant.CLASS,
                    "method" + i,
                    new int [0],
                    MethodVariant.OVERRIDABLE_IMPLEMENTATION,
                    i);
        }

        final VTableScratchArea scratchArea = new VTableScratchArea();

        final MethodFilter methodFilter = (methodNo, variant) -> methodNo % 2 == 0;

        final GetSuperType getSuperType = type -> -1;

        assertThat(methodMap.getDistinctMethodCount(
                typeNo,
                methodFilter,
                getSuperType,
                scratchArea)).isEqualTo(numMethods / 2);

        final int [] vtable = scratchArea.copyVTable();
        assertThat(vtable.length).isEqualTo(numMethods / 2);
    }

    @Test
    public void testGetDistinctMethodsWithSuperType() {

        final MethodMap methodMap = new MethodMap();

        final int superTypeNo = 1;
        final int typeNo = 2;

        final int numSuperMethods = 50;
        final int numMethods = numSuperMethods * 2;

        methodMap.allocateMethods(superTypeNo, numSuperMethods);

        for (int i = 0; i < numSuperMethods; ++ i) {

            methodMap.addMethod(
                    superTypeNo,
                    TypeVariant.CLASS,
                    "method" + i,
                    new int [0],
                    MethodVariant.OVERRIDABLE_IMPLEMENTATION,
                    i);
        }

        methodMap.allocateMethods(typeNo, numMethods);

        for (int i = 0; i < numMethods; ++ i) {

            methodMap.addMethod(
                    typeNo,
                    TypeVariant.CLASS,
                    "method" + i,
                    new int [0],
                    MethodVariant.OVERRIDABLE_IMPLEMENTATION,
                    i);
        }

        final VTableScratchArea scratchArea = new VTableScratchArea();

        final MethodFilter methodFilter = (methodNo, variant) -> true;

        final GetSuperType getSuperType = type -> type == typeNo ? superTypeNo : -1;

        assertThat(methodMap.getDistinctMethodCount(
                typeNo,
                methodFilter,
                getSuperType,
                scratchArea)).isEqualTo(numMethods);

        final int [] vtable = scratchArea.copyVTable();
        assertThat(vtable.length).isEqualTo(numMethods);
    }
}
