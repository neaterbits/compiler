package com.neaterbits.compiler.codemap;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

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
				new int [] {
						Encode.encodeType(typeNo, TypeVariant.CLASS)
				},
				methodMap);

		assertThat(methodOverrideMap.getNumberOfMethodsDirectlyExtending(methodNo)).isEqualTo(1);
		assertThat(methodOverrideMap.getMethodsDirectlyExtending(methodNo).length).isEqualTo(1);

		assertThat(Encode.decodeMethodNo(methodOverrideMap.getMethodsDirectlyExtending(methodNo)[0])).isEqualTo(overrideMethodNo);

		assertThat(methodOverrideMap.getMethodsDirectlyExtending(methodNo)[0]).isEqualTo(
				Encode.encodeMethod(overrideMethodNo, TypeVariant.CLASS, MethodVariant.FINAL_IMPLEMENTATION));

		assertThat(methodOverrideMap.getNumberOfMethodsDirectlyExtendedBy(overrideMethodNo)).isEqualTo(1);
		assertThat(methodOverrideMap.getMethodsDirectlyExtendedBy(overrideMethodNo).length).isEqualTo(1);
		assertThat(methodOverrideMap.getMethodsDirectlyExtendedBy(overrideMethodNo)[0]).isEqualTo(
				Encode.encodeMethod(methodNo, TypeVariant.CLASS, MethodVariant.OVERRIDABLE_IMPLEMENTATION));
	}

}
