package com.neaterbits.compiler.codemap;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.neaterbits.compiler.types.FieldInfo;
import com.neaterbits.compiler.types.MethodInfo;
import com.neaterbits.compiler.types.MethodVariant;
import com.neaterbits.compiler.types.Mutability;
import com.neaterbits.compiler.types.Visibility;

public class IntCodeMapTest {

    @Test
    public void testAddTypeHierarchy() {

        final IntCodeMap codeMap = new IntCodeMap(new StaticMethodOverrideMap());

        final int type1 = codeMap.addType(
                TypeVariant.CLASS,
                null,
                null);

        assertThat(type1).isGreaterThanOrEqualTo(0);

        final int baseClass = codeMap.addType(
                TypeVariant.CLASS,
                null,
                null);

        assertThat(baseClass).isGreaterThanOrEqualTo(0);

        final int interface1 = codeMap.addType(
                TypeVariant.INTERFACE,
                null,
                null);

        assertThat(type1).isGreaterThanOrEqualTo(0);

        final int interface2 = codeMap.addType(
                TypeVariant.INTERFACE,
                null,
                new int [] { interface1 });

        assertThat(type1).isGreaterThanOrEqualTo(0);

        final int subClass = codeMap.addType(
                TypeVariant.CLASS,
                new int [] { baseClass },
                new int [] { interface1, interface2 });

        assertThat(subClass).isGreaterThanOrEqualTo(0);

        // baseClass
        assertThat(codeMap.getExtendsFromSingleSuperClass(baseClass)).isEqualTo(-1);

        assertThat(codeMap.getTypesDirectlyExtendingThis(baseClass))
            .isEqualTo(new int [] { subClass });

        assertThat(codeMap.getTypesThisDirectlyExtends(baseClass))
            .isEqualTo(new int [0]);

        assertThat(codeMap.getAllTypesExtendingThis(baseClass))
            .containsOnly(subClass);

        // subClass
        assertThat(codeMap.getExtendsFromSingleSuperClass(subClass)).isEqualTo(baseClass);

        assertThat(codeMap.getTypesDirectlyExtendingThis(subClass))
            .isEqualTo(new int [0]);

        assertThat(codeMap.getTypesThisDirectlyExtends(subClass))
            .isEqualTo(new int [] { baseClass, interface1, interface2 });

        assertThat(codeMap.getAllTypesExtendingThis(subClass)).isNull();

        // interface1
        assertThat(codeMap.getTypesDirectlyExtendingThis(interface1))
            .isEqualTo(new int [] { interface2, subClass });

        assertThat(codeMap.getTypesThisDirectlyExtends(interface1))
            .isEqualTo(new int [0]);

        assertThat(codeMap.getAllTypesExtendingThis(interface1))
            .containsOnly(subClass, interface2);

        // interface2
        assertThat(codeMap.getTypesDirectlyExtendingThis(interface2))
            .isEqualTo(new int [] { subClass });

        assertThat(codeMap.getTypesThisDirectlyExtends(interface2))
            .isEqualTo(new int [] { interface1 });

        assertThat(codeMap.getAllTypesExtendingThis(interface2))
            .containsOnly(subClass);
    }

    @Test
    public void testAddMethods() {

        final IntCodeMap codeMap = new IntCodeMap(new StaticMethodOverrideMap());

        final int returnType = codeMap.addType(
                TypeVariant.CLASS,
                null,
                null);

        final int baseClass = codeMap.addType(
                TypeVariant.CLASS,
                null,
                null);

        final int subClass = codeMap.addType(
                TypeVariant.CLASS,
                new int [] { baseClass },
                null);

        final int methodNo = codeMap.addOrGetMethod(
                baseClass,
                "someMethod",
                MethodVariant.OVERRIDABLE_IMPLEMENTATION,
                returnType,
                new int[0],
                3);

        final int subMethodNo = codeMap.addOrGetExtendingMethod(
                methodNo,
                MethodVariant.OVERRIDABLE_IMPLEMENTATION,
                subClass,
                "someMethod",
                MethodVariant.FINAL_IMPLEMENTATION,
                returnType,
                new int[0],
                1);

        final VTableScratchArea scratchArea = new VTableScratchArea();

        // baseClass
        assertThat(codeMap.getIndexForMethod(methodNo)).isEqualTo(3);

        assertThat(codeMap.addOrGetMethod(
                baseClass,
                "someMethod",
                MethodVariant.OVERRIDABLE_IMPLEMENTATION,
                returnType,
                new int[0],
                3)).isEqualTo(methodNo);

        assertThat(codeMap.getMethodVariant(methodNo)).isEqualTo(MethodVariant.OVERRIDABLE_IMPLEMENTATION);
        assertThat(codeMap.getTypeForMethod(methodNo)).isEqualTo(baseClass);

        MethodInfo methodInfo = codeMap.getMethodInfo(baseClass, "someMethod", new int [0]);

        assertThat(methodInfo).isNotNull();
        assertThat(methodInfo.getMethodNo()).isEqualTo(methodNo);
        assertThat(methodInfo.getMethodVariant()).isEqualTo(MethodVariant.OVERRIDABLE_IMPLEMENTATION);

        assertThat(codeMap.getMethodsDirectlyExtending(methodNo))
            .isEqualTo(new int [] { subMethodNo });

        assertThat(codeMap.getDistinctMethodCount(baseClass, (method, variant) -> true, scratchArea))
            .isEqualTo(1);

        // subClass
        assertThat(codeMap.getIndexForMethod(subMethodNo)).isEqualTo(1);

        assertThat(codeMap.addOrGetExtendingMethod(
                methodNo,
                MethodVariant.OVERRIDABLE_IMPLEMENTATION,
                subClass,
                "someMethod",
                MethodVariant.FINAL_IMPLEMENTATION,
                returnType,
                new int[0],
                1)).isEqualTo(subMethodNo);

        assertThat(codeMap.getMethodVariant(subMethodNo)).isEqualTo(MethodVariant.FINAL_IMPLEMENTATION);
        assertThat(codeMap.getTypeForMethod(subMethodNo)).isEqualTo(subClass);

        methodInfo = codeMap.getMethodInfo(subClass, "someMethod", new int [0]);
        assertThat(methodInfo).isNotNull();
        assertThat(methodInfo.getMethodNo()).isEqualTo(subMethodNo);
        assertThat(methodInfo.getMethodVariant()).isEqualTo(MethodVariant.FINAL_IMPLEMENTATION);

        assertThat(codeMap.getMethodsDirectlyExtending(subMethodNo)).isNull();

        scratchArea.clear();
        assertThat(codeMap.getDistinctMethodCount(subClass, (method, variant) -> true, scratchArea))
            .isEqualTo(1);
    }

    @Test
    public void testGetUnknownMethod() {

        final IntCodeMap codeMap = new IntCodeMap(new StaticMethodOverrideMap());

        final int returnType = codeMap.addType(
                TypeVariant.CLASS,
                null,
                null);

        final int baseClass = codeMap.addType(
                TypeVariant.CLASS,
                null,
                null);

        codeMap.addOrGetMethod(
                baseClass,
                "someMethod",
                MethodVariant.OVERRIDABLE_IMPLEMENTATION,
                returnType,
                new int[0],
                3);

        assertThat(codeMap.getMethodInfo(baseClass, "someOtherMethod", new int[0]))
            .isNull();
    }

    @Test
    public void testComputeMethods() {

        final IntCodeMap codeMap = new IntCodeMap(new StaticMethodOverrideMap());

        final int returnType = codeMap.addType(
                TypeVariant.CLASS,
                null,
                null);

        final int baseClass = codeMap.addType(
                TypeVariant.CLASS,
                null,
                null);

        final int subClass = codeMap.addType(
                TypeVariant.CLASS,
                new int [] { baseClass },
                null);

        codeMap.setMethodCount(baseClass, 1);

        final int methodNo = codeMap.addOrGetMethod(
                baseClass,
                "someMethod",
                MethodVariant.OVERRIDABLE_IMPLEMENTATION,
                returnType,
                new int[0],
                3);

        codeMap.setMethodCount(subClass, 1);

        final int subMethodNo = codeMap.addOrGetMethod(
                subClass,
                "someMethod",
                MethodVariant.FINAL_IMPLEMENTATION,
                returnType,
                new int[0],
                1);

        codeMap.computeMethodExtends(subClass);

        assertThat(codeMap.getMethodsDirectlyExtending(methodNo))
            .isEqualTo(new int [] { subMethodNo });
    }

    @Test
    public void testAddFields() {

        final IntCodeMap codeMap = new IntCodeMap(new StaticMethodOverrideMap());

        final int fieldType = codeMap.addType(
                TypeVariant.CLASS,
                null,
                null);

        final int baseClass = codeMap.addType(
                TypeVariant.CLASS,
                null,
                null);

        final int fieldNo = codeMap.addField(
                baseClass,
                "someField",
                fieldType,
                true,
                Visibility.NAMESPACE,
                Mutability.VALUE_OR_OBJECT_IMMUTABLE,
                false,
                true,
                1);

        final FieldInfo fieldInfo = codeMap.getFieldInfo(baseClass, "someField");

        assertThat(fieldInfo.getFieldNo()).isEqualTo(fieldNo);
        assertThat(fieldInfo.getFieldType()).isEqualTo(fieldType);
        assertThat(fieldInfo.getVisibility()).isEqualTo(Visibility.NAMESPACE);
        assertThat(fieldInfo.getMutability()).isEqualTo(Mutability.VALUE_OR_OBJECT_IMMUTABLE);
    }

    @Test
    public void testGetUnknownField() {

        final IntCodeMap codeMap = new IntCodeMap(new StaticMethodOverrideMap());

        final int fieldType = codeMap.addType(
                TypeVariant.CLASS,
                null,
                null);

        final int baseClass = codeMap.addType(
                TypeVariant.CLASS,
                null,
                null);

        codeMap.addField(
                baseClass,
                "someField",
                fieldType,
                true,
                Visibility.NAMESPACE,
                Mutability.VALUE_OR_OBJECT_IMMUTABLE,
                false,
                true,
                1);

        assertThat(codeMap.getFieldInfo(baseClass, "someOtherField")).isNull();
    }
}
