package com.neaterbits.language.codemap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.neaterbits.language.codemap.ArrayAllocation;
import com.neaterbits.language.codemap.Encode;
import com.neaterbits.language.codemap.MethodMap;
import com.neaterbits.language.codemap.StaticMethodOverrideMap;
import com.neaterbits.language.codemap.MethodOverrideMap.GetExtendedTypesEncoded;
import com.neaterbits.language.common.types.MethodVariant;
import com.neaterbits.language.common.types.TypeVariant;

public class StaticMethodOverrideMapTest {

    @Test
    public void testNotExtendingForNoMethodsAddedYet() {

        final StaticMethodOverrideMap map = new StaticMethodOverrideMap();

        final int methodNo = 1;

        assertThat(map.getNumberOfMethodsDirectlyExtending(methodNo))
            .isEqualTo(0);

        assertThat(map.getMethodsDirectlyExtending(methodNo))
            .isNull();

        assertThat(map.getNumberOfMethodsDirectlyExtendedBy(methodNo))
            .isEqualTo(0);

        assertThat(map.getMethodsDirectlyExtendedBy(methodNo))
            .isNull();
    }

    @Test
    public void testNotExtendingForMethodAfterLastIndex() {

        final StaticMethodOverrideMap map = new StaticMethodOverrideMap();

        final int extendedMethod = 1;
        final MethodVariant extendedMethodVariant = MethodVariant.OVERRIDABLE_IMPLEMENTATION;

        final int extendingMethod = 2;
        final MethodVariant extendingMethodVariant = MethodVariant.FINAL_IMPLEMENTATION;

        map.addMethodExtends(
                extendedMethod, extendedMethodVariant,
                extendingMethod, extendingMethodVariant);

        final int methodNo = 1000000;

        assertThat(methodNo).isGreaterThan(ArrayAllocation.DEFAULT_LENGTH);

        assertThat(map.getNumberOfMethodsDirectlyExtending(methodNo))
            .isEqualTo(0);

        assertThat(map.getMethodsDirectlyExtending(methodNo))
            .isNull();

        assertThat(map.getNumberOfMethodsDirectlyExtendedBy(methodNo))
            .isEqualTo(0);

        assertThat(map.getMethodsDirectlyExtendedBy(methodNo))
            .isNull();
    }

    @Test
    public void testNotExtendingForMethodAtNullIndex() {

        final StaticMethodOverrideMap map = new StaticMethodOverrideMap();

        final int extendedMethod = 1;
        final MethodVariant extendedMethodVariant = MethodVariant.OVERRIDABLE_IMPLEMENTATION;

        final int extendingMethod = 2;
        final MethodVariant extendingMethodVariant = MethodVariant.FINAL_IMPLEMENTATION;

        map.addMethodExtends(
                extendedMethod, extendedMethodVariant,
                extendingMethod, extendingMethodVariant);

        final int methodNo = 3;

        assertThat(methodNo).isLessThan(ArrayAllocation.DEFAULT_LENGTH);

        assertThat(map.getNumberOfMethodsDirectlyExtending(methodNo))
            .isEqualTo(0);

        assertThat(map.getMethodsDirectlyExtending(methodNo))
            .isNull();

        assertThat(map.getNumberOfMethodsDirectlyExtendedBy(methodNo))
            .isEqualTo(0);

        assertThat(map.getMethodsDirectlyExtendedBy(methodNo))
            .isNull();
    }

    @Test
    public void testExtendingOneLevel() {

        final StaticMethodOverrideMap map = new StaticMethodOverrideMap();

        final int extendedMethod = 1;
        final MethodVariant extendedMethodVariant = MethodVariant.OVERRIDABLE_IMPLEMENTATION;

        final int extendingMethod = 2;
        final MethodVariant extendingMethodVariant = MethodVariant.FINAL_IMPLEMENTATION;

        map.addMethodExtends(
                extendedMethod, extendedMethodVariant,
                extendingMethod, extendingMethodVariant);

        // extendedMethod
        assertThat(map.getNumberOfMethodsDirectlyExtendedBy(extendedMethod))
            .isEqualTo(0);

        assertThat(map.getMethodsDirectlyExtendedBy(extendedMethod))
            .isNull();

        assertThat(map.getNumberOfMethodsDirectlyExtending(extendedMethod))
            .isEqualTo(1);

        assertThat(map.getMethodsDirectlyExtending(extendedMethod))
            .isEqualTo(new int [] { extendingMethod });

        // extendingMethod
        assertThat(map.getNumberOfMethodsDirectlyExtendedBy(extendingMethod))
            .isEqualTo(1);

        assertThat(map.getMethodsDirectlyExtendedBy(extendingMethod))
            .isEqualTo(new int [] { extendedMethod });

        assertThat(map.getNumberOfMethodsDirectlyExtending(extendingMethod))
            .isEqualTo(0);

        assertThat(map.getMethodsDirectlyExtending(extendingMethod))
            .isNull();

        // Total
        assertThat(map.getTotalNumberOfMethodsExtending(extendedMethod))
            .isEqualTo(1);

        assertThat(map.getTotalNumberOfMethodsExtending(extendingMethod))
            .isEqualTo(0);
    }

    @Test
    public void testExtendingMultipleLevels() {

        final StaticMethodOverrideMap map = new StaticMethodOverrideMap();

        final int extendedMethod = 1;
        final MethodVariant extendedMethodVariant = MethodVariant.OVERRIDABLE_IMPLEMENTATION;

        final int extendingMethod = 2;
        final MethodVariant extendingMethodVariant = MethodVariant.OVERRIDABLE_IMPLEMENTATION;

        final int extendingTheExtendingMethod = 3;
        final MethodVariant extendingTheExtendingMethodVariant = MethodVariant.FINAL_IMPLEMENTATION;

        map.addMethodExtends(
                extendedMethod, extendedMethodVariant,
                extendingMethod, extendingMethodVariant);

        map.addMethodExtends(
                extendingMethod, extendingMethodVariant,
                extendingTheExtendingMethod, extendingTheExtendingMethodVariant);

        // extendedMethod
        assertThat(map.getNumberOfMethodsDirectlyExtending(extendedMethod))
            .isEqualTo(1);

        assertThat(map.getMethodsDirectlyExtending(extendedMethod))
            .isEqualTo(new int [] { extendingMethod });

        assertThat(map.getNumberOfMethodsDirectlyExtendedBy(extendedMethod))
            .isEqualTo(0);

        assertThat(map.getMethodsDirectlyExtendedBy(extendedMethod))
            .isNull();

        // extendingMethod
        assertThat(map.getNumberOfMethodsDirectlyExtendedBy(extendingMethod))
            .isEqualTo(1);

        assertThat(map.getMethodsDirectlyExtendedBy(extendingMethod))
            .isEqualTo(new int [] { extendedMethod });

        assertThat(map.getNumberOfMethodsDirectlyExtending(extendingMethod))
            .isEqualTo(1);

        assertThat(map.getMethodsDirectlyExtending(extendingMethod))
            .isEqualTo(new int [] { extendingTheExtendingMethod });

        // extendingTheExtendingMethod
        assertThat(map.getNumberOfMethodsDirectlyExtendedBy(extendingTheExtendingMethod))
            .isEqualTo(1);

        assertThat(map.getMethodsDirectlyExtendedBy(extendingTheExtendingMethod))
            .isEqualTo(new int [] { extendingMethod });

        assertThat(map.getNumberOfMethodsDirectlyExtending(extendingTheExtendingMethod))
            .isEqualTo(0);

        assertThat(map.getMethodsDirectlyExtending(extendingTheExtendingMethod))
            .isNull();

        // Total number of methods
        assertThat(map.getTotalNumberOfMethodsExtending(extendedMethod))
            .isEqualTo(2);

        assertThat(map.getTotalNumberOfMethodsExtending(extendingMethod))
            .isEqualTo(1);

        assertThat(map.getTotalNumberOfMethodsExtending(extendingTheExtendingMethod))
            .isEqualTo(0);
    }

    @Test
    public void testAddingStaticExtendedMethodThrowsException() {

        final StaticMethodOverrideMap map = new StaticMethodOverrideMap();

        final int extendedMethod = 1;
        final MethodVariant extendedMethodVariant = MethodVariant.STATIC;

        final int extendingMethod = 2;
        final MethodVariant extendingMethodVariant = MethodVariant.FINAL_IMPLEMENTATION;

        try {
            map.addMethodExtends(
                    extendedMethod, extendedMethodVariant,
                    extendingMethod, extendingMethodVariant);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {

        }
    }

    @Test
    public void testAddingStaticExtendingMethodThrowsException() {

        final StaticMethodOverrideMap map = new StaticMethodOverrideMap();

        final int extendedMethod = 1;
        final MethodVariant extendedMethodVariant = MethodVariant.OVERRIDABLE_IMPLEMENTATION;

        final int extendingMethod = 2;
        final MethodVariant extendingMethodVariant = MethodVariant.STATIC;

        try {
            map.addMethodExtends(
                    extendedMethod, extendedMethodVariant,
                    extendingMethod, extendingMethodVariant);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {

        }
    }

    @Test
    public void testOverridingFinalMethodThrowsException() {

        final StaticMethodOverrideMap map = new StaticMethodOverrideMap();

        final int extendedMethod = 1;
        final MethodVariant extendedMethodVariant = MethodVariant.FINAL_IMPLEMENTATION;

        final int extendingMethod = 2;
        final MethodVariant extendingMethodVariant = MethodVariant.OVERRIDABLE_IMPLEMENTATION;

        try {
            map.addMethodExtends(
                    extendedMethod, extendedMethodVariant,
                    extendingMethod, extendingMethodVariant);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {

        }
    }

    @Test
    public void testTypeExtendsType() {

        final int type1 = 1;
        final int type2 = 2;
        final int type3 = 3;

        final StaticMethodOverrideMap map = new StaticMethodOverrideMap();

        final MethodMap methodMap = new MethodMap();

        final int methodNo1 = methodMap.addMethod(
                type1,
                TypeVariant.CLASS,
                "someMethod",
                new int[0],
                MethodVariant.OVERRIDABLE_IMPLEMENTATION,
                0);

        final int methodNo2 = methodMap.addMethod(
                type2,
                TypeVariant.CLASS,
                "someMethod",
                new int[0],
                MethodVariant.OVERRIDABLE_IMPLEMENTATION,
                0);

        final int methodNo3 = methodMap.addMethod(
                type3,
                TypeVariant.CLASS,
                "someMethod",
                new int[0],
                MethodVariant.OVERRIDABLE_IMPLEMENTATION,
                0);

        final GetExtendedTypesEncoded getExtendedTypesEncoded = makeGetExtendedTypesEncoded(type1, type2, type3, true);

        map.addTypeExtendsTypes(
                Encode.encodeType(type2, TypeVariant.CLASS),
                getExtendedTypesEncoded,
                methodMap);

        map.addTypeExtendsTypes(
                Encode.encodeType(type3, TypeVariant.CLASS),
                getExtendedTypesEncoded,
                methodMap);

        // methodNo1
        assertThat(map.getNumberOfMethodsDirectlyExtendedBy(methodNo1))
        .isEqualTo(0);

        assertThat(map.getMethodsDirectlyExtendedBy(methodNo1))
            .isNull();

        assertThat(map.getNumberOfMethodsDirectlyExtending(methodNo1))
            .isEqualTo(1);

        assertThat(map.getMethodsDirectlyExtending(methodNo1))
            .isEqualTo(new int [] { methodNo2 });

        // methodNo2
        assertThat(map.getNumberOfMethodsDirectlyExtendedBy(methodNo2))
            .isEqualTo(1);

        assertThat(map.getMethodsDirectlyExtendedBy(methodNo2))
        .isEqualTo(new int [] { methodNo1 });

        assertThat(map.getNumberOfMethodsDirectlyExtending(methodNo2))
            .isEqualTo(1);

        assertThat(map.getMethodsDirectlyExtending(methodNo2))
            .isEqualTo(new int [] { methodNo3 });

        // methodNo3
        assertThat(map.getNumberOfMethodsDirectlyExtendedBy(methodNo3))
            .isEqualTo(1);

        assertThat(map.getMethodsDirectlyExtendedBy(methodNo3))
        .isEqualTo(new int [] { methodNo2 });

        assertThat(map.getNumberOfMethodsDirectlyExtending(methodNo3))
            .isEqualTo(0);

        assertThat(map.getMethodsDirectlyExtending(methodNo3))
            .isNull();

        assertThat(map.getTotalNumberOfMethodsExtending(methodNo1))
            .isEqualTo(2);

        assertThat(map.getTotalNumberOfMethodsExtending(methodNo2))
            .isEqualTo(1);

        assertThat(map.getTotalNumberOfMethodsExtending(methodNo3))
            .isEqualTo(0);
    }

    @Test
    public void testTypeExtendsTypeWithNoMethodsExtending() {

        final int type1 = 1;
        final int type2 = 2;
        final int type3 = 3;

        final StaticMethodOverrideMap map = new StaticMethodOverrideMap();

        final MethodMap methodMap = new MethodMap();

        final int methodNo1 = methodMap.addMethod(
                type1,
                TypeVariant.CLASS,
                "someMethod",
                new int[0],
                MethodVariant.OVERRIDABLE_IMPLEMENTATION,
                0);

        final int methodNo2 = methodMap.addMethod(
                type2,
                TypeVariant.CLASS,
                "someOtherMethod",
                new int[0],
                MethodVariant.OVERRIDABLE_IMPLEMENTATION,
                0);

        final int methodNo3 = methodMap.addMethod(
                type3,
                TypeVariant.CLASS,
                "yetAnotherMethod",
                new int[0],
                MethodVariant.OVERRIDABLE_IMPLEMENTATION,
                0);

        final GetExtendedTypesEncoded getExtendedTypesEncoded = makeGetExtendedTypesEncoded(type1, type2, type3, false);

        map.addTypeExtendsTypes(
                Encode.encodeType(type2, TypeVariant.CLASS),
                getExtendedTypesEncoded,
                methodMap);

        map.addTypeExtendsTypes(
                Encode.encodeType(type3, TypeVariant.CLASS),
                getExtendedTypesEncoded,
                methodMap);

        // methodNo1
        assertThat(map.getNumberOfMethodsDirectlyExtendedBy(methodNo1))
            .isEqualTo(0);

        assertThat(map.getMethodsDirectlyExtendedBy(methodNo1))
            .isNull();

        assertThat(map.getNumberOfMethodsDirectlyExtending(methodNo1))
            .isEqualTo(0);

        assertThat(map.getMethodsDirectlyExtending(methodNo1))
            .isNull();

        // methodNo2
        assertThat(map.getNumberOfMethodsDirectlyExtendedBy(methodNo2))
            .isEqualTo(0);

        assertThat(map.getMethodsDirectlyExtendedBy(methodNo2))
            .isNull();

        assertThat(map.getNumberOfMethodsDirectlyExtending(methodNo2))
            .isEqualTo(0);

        assertThat(map.getMethodsDirectlyExtending(methodNo2))
            .isNull();

        // methodNo3
        assertThat(map.getNumberOfMethodsDirectlyExtendedBy(methodNo3))
            .isEqualTo(0);

        assertThat(map.getMethodsDirectlyExtendedBy(methodNo3))
            .isNull();

        assertThat(map.getNumberOfMethodsDirectlyExtending(methodNo3))
            .isEqualTo(0);

        assertThat(map.getMethodsDirectlyExtending(methodNo3))
            .isNull();

        assertThat(map.getTotalNumberOfMethodsExtending(methodNo1))
            .isEqualTo(0);

        assertThat(map.getTotalNumberOfMethodsExtending(methodNo2))
            .isEqualTo(0);

        assertThat(map.getTotalNumberOfMethodsExtending(methodNo3))
            .isEqualTo(0);
    }

    @Test
    public void testTypeExtendsTypeWithExtendInSubClassOfSubClass() {

        final int type1 = 1;
        final int type2 = 2;
        final int type3 = 3;

        final StaticMethodOverrideMap map = new StaticMethodOverrideMap();

        final MethodMap methodMap = new MethodMap();

        final int methodNo1 = methodMap.addMethod(
                type1,
                TypeVariant.CLASS,
                "someMethod",
                new int[0],
                MethodVariant.OVERRIDABLE_IMPLEMENTATION,
                0);

        final int methodNo2 = methodMap.addMethod(
                type2,
                TypeVariant.CLASS,
                "someOtherMethod",
                new int[0],
                MethodVariant.OVERRIDABLE_IMPLEMENTATION,
                0);

        final int methodNo3 = methodMap.addMethod(
                type3,
                TypeVariant.CLASS,
                "someMethod",
                new int[0],
                MethodVariant.OVERRIDABLE_IMPLEMENTATION,
                0);

        final GetExtendedTypesEncoded getExtendedTypesEncoded = makeGetExtendedTypesEncoded(type1, type2, type3, false);

        map.addTypeExtendsTypes(
                Encode.encodeType(type2, TypeVariant.CLASS),
                getExtendedTypesEncoded,
                methodMap);

        map.addTypeExtendsTypes(
                Encode.encodeType(type3, TypeVariant.CLASS),
                getExtendedTypesEncoded,
                methodMap);

        // methodNo1
        assertThat(map.getNumberOfMethodsDirectlyExtendedBy(methodNo1))
        .isEqualTo(0);

        assertThat(map.getMethodsDirectlyExtendedBy(methodNo1))
            .isNull();

        assertThat(map.getNumberOfMethodsDirectlyExtending(methodNo1))
            .isEqualTo(1);

        assertThat(map.getMethodsDirectlyExtending(methodNo1))
            .isEqualTo(new int [] { methodNo3 });

        // methodNo2
        assertThat(map.getNumberOfMethodsDirectlyExtendedBy(methodNo2))
            .isEqualTo(0);

        assertThat(map.getMethodsDirectlyExtendedBy(methodNo2))
            .isNull();

        assertThat(map.getNumberOfMethodsDirectlyExtending(methodNo2))
            .isEqualTo(0);

        assertThat(map.getMethodsDirectlyExtending(methodNo2))
            .isNull();

        // methodNo3
        assertThat(map.getNumberOfMethodsDirectlyExtendedBy(methodNo3))
            .isEqualTo(1);

        assertThat(map.getMethodsDirectlyExtendedBy(methodNo3))
            .isEqualTo(new int [] { methodNo1 });

        assertThat(map.getNumberOfMethodsDirectlyExtending(methodNo3))
            .isEqualTo(0);

        assertThat(map.getMethodsDirectlyExtending(methodNo3))
            .isNull();

        assertThat(map.getTotalNumberOfMethodsExtending(methodNo1))
            .isEqualTo(1);

        assertThat(map.getTotalNumberOfMethodsExtending(methodNo2))
            .isEqualTo(0);

        assertThat(map.getTotalNumberOfMethodsExtending(methodNo3))
            .isEqualTo(0);
    }

    private static GetExtendedTypesEncoded makeGetExtendedTypesEncoded(int type1, int type2, int type3, boolean returnEmptyArray) {

        final GetExtendedTypesEncoded getExtendedTypesEncoded = type -> {

            final int [] extendedTypesEncoded;

            if (type == type2) {
                extendedTypesEncoded = new int [] { Encode.encodeType(type1, TypeVariant.CLASS) };
            }
            else if (type == type3) {
                extendedTypesEncoded = new int [] { Encode.encodeType(type2, TypeVariant.CLASS) };
            }
            else {
                extendedTypesEncoded = returnEmptyArray ? new int[0] : null;
            }

            return extendedTypesEncoded;
        };

        return getExtendedTypesEncoded;
    }
}
