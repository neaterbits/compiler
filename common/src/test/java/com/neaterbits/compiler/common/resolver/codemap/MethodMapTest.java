package com.neaterbits.compiler.common.resolver.codemap;

import org.junit.Test;

import com.neaterbits.compiler.common.loader.TypeVariant;

import static org.assertj.core.api.Assertions.assertThat;

public class MethodMapTest {

	@Test
	public void testAddMethod() {
		
		final int typeNo = 1;
		
		final int paramType1 = 2;
		final int paramType2 = 3;
		
		final MethodMapCache cache = new MethodMapCache();
		
		final MethodMap methodMap = new MethodMap();
		
		methodMap.allocateMethods(typeNo, 3);
		
		final int methodNo = methodMap.addMethod(
				typeNo,
				TypeVariant.CLASS,
				"someMethod",
				new int [] { paramType1, paramType2 },
				MethodVariant.OVERRIDABLE_IMPLEMENTATION,
				cache);
		
		assertThat(methodNo).isEqualTo(0);

		final int anotherMethodNo = methodMap.addMethod(
				typeNo,
				TypeVariant.CLASS,
				"someOtherMethod",
				new int [] { paramType1 },
				MethodVariant.FINAL_IMPLEMENTATION,
				cache);
		
		assertThat(anotherMethodNo).isEqualTo(1);

		final int anotherTypeNo = 4;

		methodMap.allocateMethods(anotherTypeNo, 1);
		
		final int overrideMethodNo = methodMap.addMethod(
				anotherTypeNo,
				TypeVariant.CLASS,
				"someMethod",
				new int [] { paramType1, paramType2 },
				MethodVariant.FINAL_IMPLEMENTATION,
				cache);

		assertThat(overrideMethodNo).isEqualTo(2);

		assertThat(methodMap.getMethodNo(1, "someMethod", new int [] { paramType1, paramType2 }, cache)).isEqualTo(0);
		assertThat(methodMap.getMethodNo(1, "notSomeMethod", new int [] { paramType1, paramType2 }, cache)).isEqualTo(-1);

		assertThat(methodMap.getMethodNo(1, "someMethod", new int [] { paramType1 }, cache)).isEqualTo(-1);
		assertThat(methodMap.getMethodNo(1, "someOtherMethod", new int [] { paramType1 }, cache)).isEqualTo(1);
		assertThat(methodMap.getMethodNo(4, "someMethod", new int [] { paramType1, paramType2 }, cache)).isEqualTo(2);

		assertThat(methodMap.getMethodName(methodNo)).isEqualTo("someMethod");
		assertThat(methodMap.getMethodName(anotherMethodNo)).isEqualTo("someOtherMethod");
		assertThat(methodMap.getMethodName(overrideMethodNo)).isEqualTo("someMethod");

		assertThat(methodMap.getMethodParameterTypes(methodNo)).containsExactly(paramType1, paramType2);
		assertThat(methodMap.getMethodParameterTypes(anotherMethodNo)).containsExactly(paramType1);
		assertThat(methodMap.getMethodParameterTypes(overrideMethodNo)).containsExactly(paramType1, paramType2);
		
		methodMap.addTypeExtendsTypes(
				Encode.encodeType(anotherTypeNo, TypeVariant.CLASS),
				new int [] { 
						Encode.encodeType(typeNo, TypeVariant.CLASS)
				});
		
		assertThat(methodMap.getNumberOfMethodsDirectlyExtending(methodNo)).isEqualTo(1);
		assertThat(methodMap.getMethodsDirectlyExtending(methodNo).length).isEqualTo(1);
		assertThat(methodMap.getMethodsDirectlyExtending(methodNo)[0]).isEqualTo(
				Encode.encodeMethodWithoutTypeVariant(overrideMethodNo, MethodVariant.FINAL_IMPLEMENTATION));

		assertThat(methodMap.getNumberOfMethodsDirectlyExtendedBy(overrideMethodNo)).isEqualTo(1);
		assertThat(methodMap.getMethodsDirectlyExtendedBy(overrideMethodNo).length).isEqualTo(1);
		assertThat(methodMap.getMethodsDirectlyExtendedBy(overrideMethodNo)[0]).isEqualTo(
				Encode.encodeMethodWithoutTypeVariant(methodNo, MethodVariant.OVERRIDABLE_IMPLEMENTATION));
	}
	
}
