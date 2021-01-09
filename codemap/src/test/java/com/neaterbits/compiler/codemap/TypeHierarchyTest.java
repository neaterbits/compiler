package com.neaterbits.compiler.codemap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.neaterbits.compiler.types.TypeVariant;

public class TypeHierarchyTest {

    @Test
    public void testHierarchy() {

        final TypeHierarchy typeHierarchy = new TypeHierarchy();

        final int baseClass = typeHierarchy.addType(
                TypeVariant.CLASS,
                null,
                null);

        final int interface1 = typeHierarchy.addType(
                TypeVariant.INTERFACE,
                null,
                null);

        final int interface2 = typeHierarchy.addType(
                TypeVariant.INTERFACE,
                new int [] { Encode.encodeType(interface1, TypeVariant.INTERFACE) },
                null);

        final int subClass = typeHierarchy.addType(
                TypeVariant.CLASS,
                new int [] { Encode.encodeType(baseClass, TypeVariant.CLASS) },
                new int [] { Encode.encodeType(interface2, TypeVariant.INTERFACE) });

        final int interface3 = typeHierarchy.addType(
                TypeVariant.INTERFACE,
                null,
                new int [] { Encode.encodeType(interface2, TypeVariant.INTERFACE) });

        // baseClass
        assertThat(typeHierarchy.getNumExtendingThis(baseClass))
            .isEqualTo(1);

        assertThat(typeHierarchy.getExtendsFromSingleSuperClass(baseClass))
            .isEqualTo(-1);

        assertThat(typeHierarchy.getTypesThisExtendsFrom(baseClass, typeEncoded -> true))
            .isNull();

        assertThat(typeHierarchy.getTypesThisExtendsFrom(baseClass, typeEncoded -> false))
            .isNull();

        assertThat(typeHierarchy.getTypesThisExtendsFrom(baseClass, null))
            .isNull();

        assertThat(typeHierarchy.getTypesThisExtendsFromEncoded(baseClass))
            .isNull();

        assertThat(typeHierarchy.getTypesExtendingThisEncoded(baseClass))
            .isEqualTo(new int [] { Encode.encodeType(subClass, TypeVariant.CLASS) });

        // subClass
        assertThat(typeHierarchy.getNumExtendingThis(subClass))
            .isEqualTo(0);

        assertThat(typeHierarchy.getExtendsFromSingleSuperClass(subClass))
            .isEqualTo(baseClass);

        assertThat(typeHierarchy.getTypesThisExtendsFrom(subClass, typeEncoded -> true))
            .isEqualTo(new int [] { baseClass, interface2 });

        assertThat(typeHierarchy.getTypesThisExtendsFrom(subClass, typeEncoded -> false))
            .isNull();

        assertThat(typeHierarchy.getTypesThisExtendsFrom(subClass, typeEncoded -> Encode.getTypeVariant(typeEncoded) == TypeVariant.CLASS))
            .isEqualTo(new int [] { baseClass });

        assertThat(typeHierarchy.getTypesThisExtendsFrom(subClass, typeEncoded -> Encode.getTypeVariant(typeEncoded) == TypeVariant.INTERFACE))
            .isEqualTo(new int [] { interface2 });

        assertThat(typeHierarchy.getTypesThisExtendsFrom(subClass, null))
            .isEqualTo(new int [] { baseClass, interface2 });

        assertThat(typeHierarchy.getTypesThisExtendsFromEncoded(subClass))
            .isEqualTo(
                    new int [] {
                            Encode.encodeType(baseClass, TypeVariant.CLASS),
                            Encode.encodeType(interface2, TypeVariant.INTERFACE)
            });

        assertThat(typeHierarchy.getTypesExtendingThisEncoded(subClass))
            .isNull();

        // interface1
        assertThat(typeHierarchy.getNumExtendingThis(interface1))
            .isEqualTo(1);

        assertThat(typeHierarchy.getTypesThisExtendsFrom(baseClass, typeEncoded -> true))
            .isNull();

        assertThat(typeHierarchy.getTypesThisExtendsFrom(baseClass, typeEncoded -> false))
            .isNull();

        assertThat(typeHierarchy.getTypesThisExtendsFrom(baseClass, null))
            .isNull();

        assertThat(typeHierarchy.getTypesThisExtendsFromEncoded(interface1))
            .isNull();

        assertThat(typeHierarchy.getTypesExtendingThisEncoded(interface1))
            .isEqualTo(new int [] { Encode.encodeType(interface2, TypeVariant.INTERFACE) });

        // interface2
        assertThat(typeHierarchy.getNumExtendingThis(interface2))
            .isEqualTo(2);

        assertThat(typeHierarchy.getTypesThisExtendsFrom(interface2, typeEncoded -> true))
            .isEqualTo(new int [] { interface1 });

        assertThat(typeHierarchy.getTypesThisExtendsFrom(interface2, typeEncoded -> false))
            .isNull();

        assertThat(typeHierarchy.getTypesThisExtendsFrom(subClass, typeEncoded -> Encode.getTypeVariant(typeEncoded) == TypeVariant.CLASS))
            .isEqualTo(new int [] { baseClass });

        assertThat(typeHierarchy.getTypesThisExtendsFrom(subClass, typeEncoded -> Encode.getTypeVariant(typeEncoded) == TypeVariant.INTERFACE))
            .isEqualTo(new int [] { interface2 });

        assertThat(typeHierarchy.getTypesThisExtendsFrom(interface2, null))
            .isEqualTo(new int [] { interface1 });

        assertThat(typeHierarchy.getTypesThisExtendsFromEncoded(interface2))
            .isEqualTo(
                    new int [] {
                            Encode.encodeType(interface1, TypeVariant.INTERFACE)
            });

        assertThat(typeHierarchy.getTypesExtendingThisEncoded(interface2))
            .isEqualTo(
                    new int [] {
                            Encode.encodeType(subClass, TypeVariant.CLASS),
                            Encode.encodeType(interface3, TypeVariant.INTERFACE)
            });

        // interface3
        assertThat(typeHierarchy.getNumExtendingThis(interface3))
            .isEqualTo(0);

        assertThat(typeHierarchy.getTypesThisExtendsFrom(interface3, typeEncoded -> true))
            .isEqualTo(new int [] { interface2 });

        assertThat(typeHierarchy.getTypesThisExtendsFrom(interface3, typeEncoded -> false))
            .isNull();

        assertThat(typeHierarchy.getTypesThisExtendsFrom(interface3, null))
            .isEqualTo(new int [] { interface2 });

        assertThat(typeHierarchy.getTypesThisExtendsFromEncoded(interface3))
            .isEqualTo(
                    new int [] {
                            Encode.encodeType(interface2, TypeVariant.INTERFACE)
            });

        assertThat(typeHierarchy.getTypesExtendingThisEncoded(interface3))
            .isNull();
    }

    @Test
    public void testGetExtendsFromSingleSuperClassThrowsExceptioonForInterface() {

        final TypeHierarchy typeHierarchy = new TypeHierarchy();

        final int interface1 = typeHierarchy.addType(
                TypeVariant.INTERFACE,
                null,
                null);

        final int interface2 = typeHierarchy.addType(
                TypeVariant.INTERFACE,
                new int [] { interface1 },
                null);

        try {
            typeHierarchy.getExtendsFromSingleSuperClass(interface2);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {

        }
    }

    @Test
    public void testGetSuperClassFromMultipleInheritedThrowsException() {

        final TypeHierarchy typeHierarchy = new TypeHierarchy();

        final int baseClass1 = typeHierarchy.addType(
                TypeVariant.CLASS,
                null,
                null);

        final int baseClass2 = typeHierarchy.addType(
                TypeVariant.CLASS,
                null,
                null);

        final int subClass = typeHierarchy.addType(
                TypeVariant.CLASS,
                new int [] {
                        Encode.encodeType(baseClass1, TypeVariant.CLASS),
                        Encode.encodeType(baseClass2, TypeVariant.CLASS),
                },
                null);

        try {
            typeHierarchy.getExtendsFromSingleSuperClass(subClass);

            fail("Expected exception");
        }
        catch (IllegalStateException ex) {

        }
    }

    @Test
    public void testGetTypeVariant() {

        final TypeHierarchy typeHierarchy = new TypeHierarchy();

        final int classType = typeHierarchy.addType(
                TypeVariant.CLASS,
                null,
                null);

        final int interfaceType = typeHierarchy.addType(
                TypeVariant.INTERFACE,
                null,
                null);

        final int builtinType = typeHierarchy.addType(
                TypeVariant.BUILTIN,
                null,
                null);

        final int enumType = typeHierarchy.addType(
                TypeVariant.ENUM,
                null,
                null);
        assertThat(typeHierarchy.getTypeVariantForType(classType))
            .isEqualTo(TypeVariant.CLASS);

        assertThat(typeHierarchy.getTypeVariantForType(interfaceType))
            .isEqualTo(TypeVariant.INTERFACE);

        assertThat(typeHierarchy.getTypeVariantForType(builtinType))
            .isEqualTo(TypeVariant.BUILTIN);

        assertThat(typeHierarchy.getTypeVariantForType(enumType))
            .isEqualTo(TypeVariant.ENUM);
    }

    @Test
    public void testEncodeAndGetTypeVariant() {

        final TypeHierarchy typeHierarchy = new TypeHierarchy();

        final int classType = typeHierarchy.addType(
                TypeVariant.CLASS,
                null,
                null);

        final int interfaceType = typeHierarchy.addType(
                TypeVariant.INTERFACE,
                null,
                null);

        final int builtinType = typeHierarchy.addType(
                TypeVariant.BUILTIN,
                null,
                null);

        final int enumType = typeHierarchy.addType(
                TypeVariant.ENUM,
                null,
                null);

        final int [] encoded = typeHierarchy.encodeTypeVariant(new int [] {
                classType,
                interfaceType,
                builtinType,
                enumType
        });

        assertThat(Encode.getTypeVariant(encoded[0])).isEqualTo(TypeVariant.CLASS);
        assertThat(Encode.decodeTypeNo(encoded[0])).isEqualTo(classType);

        assertThat(Encode.getTypeVariant(encoded[1])).isEqualTo(TypeVariant.INTERFACE);
        assertThat(Encode.decodeTypeNo(encoded[1])).isEqualTo(interfaceType);

        assertThat(Encode.getTypeVariant(encoded[2])).isEqualTo(TypeVariant.BUILTIN);
        assertThat(Encode.decodeTypeNo(encoded[2])).isEqualTo(builtinType);

        assertThat(Encode.getTypeVariant(encoded[3])).isEqualTo(TypeVariant.ENUM);
        assertThat(Encode.decodeTypeNo(encoded[3])).isEqualTo(enumType);
    }


	@Test
	public void testAddMany() {

		final TypeHierarchy typeHierarchy = new TypeHierarchy();

		for (int i = 0; i < 100000; ++ i) {
			final int typeNo = typeHierarchy.addType(TypeVariant.CLASS, null, null);

			assertThat(typeNo).isEqualTo(i);
		}
	}
}
