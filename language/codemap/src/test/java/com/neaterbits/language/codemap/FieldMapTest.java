package com.neaterbits.language.codemap;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.Test;

import com.neaterbits.language.codemap.FieldInfo;
import com.neaterbits.language.codemap.FieldMap;
import com.neaterbits.language.common.types.Mutability;
import com.neaterbits.language.common.types.Visibility;

public class FieldMapTest {

	@Test
	public void testAddFields() {

		final FieldMap fieldMap = new FieldMap();

		final int typeNo = 1;
		final int fieldTypeNo = 123;

		int addedField = fieldMap.addField(
				typeNo,
				"staticField",
				12,
				fieldTypeNo,
				true,
				Visibility.NAMESPACE,
				Mutability.MUTABLE,
				false,
				false);

		assertThat(addedField).isGreaterThanOrEqualTo(0);
		assertThat(fieldMap.getFieldCount(typeNo)).isEqualTo(1);
		assertThat(fieldMap.getFieldNo(typeNo, 0)).isEqualTo(addedField);
		assertThat(fieldMap.isFieldStatic(typeNo, 0)).isTrue();
		assertThat(fieldMap.getFieldVisibility(typeNo, 0)).isEqualTo(Visibility.NAMESPACE);
		assertThat(fieldMap.getFieldMutability(typeNo, 0)).isEqualTo(Mutability.MUTABLE);
		assertThat(fieldMap.isFieldVolatile(typeNo, 0)).isFalse();
		assertThat(fieldMap.isFieldTransient(typeNo, 0)).isFalse();
		assertThat(fieldMap.getFieldType(typeNo, 0)).isEqualTo(fieldTypeNo);
		assertThat(fieldMap.getFieldName(typeNo, 0)).isEqualTo("staticField");
		assertThat(fieldMap.getFieldByName(typeNo, "staticField")).isEqualTo(addedField);

		addedField = fieldMap.addField(
				typeNo,
				"publicField",
				12,
				fieldTypeNo,
				false,
				Visibility.PUBLIC,
				Mutability.MUTABLE,
				false,
				false);

		assertThat(addedField).isGreaterThanOrEqualTo(0);
		assertThat(fieldMap.getFieldCount(typeNo)).isEqualTo(2);
		assertThat(fieldMap.getFieldNo(typeNo, 1)).isEqualTo(addedField);
		assertThat(fieldMap.isFieldStatic(typeNo, 1)).isFalse();
		assertThat(fieldMap.getFieldVisibility(typeNo, 1)).isEqualTo(Visibility.PUBLIC);
		assertThat(fieldMap.getFieldMutability(typeNo, 1)).isEqualTo(Mutability.MUTABLE);
		assertThat(fieldMap.isFieldVolatile(typeNo, 1)).isFalse();
		assertThat(fieldMap.isFieldTransient(typeNo, 1)).isFalse();
		assertThat(fieldMap.getFieldType(typeNo, 1)).isEqualTo(fieldTypeNo);
		assertThat(fieldMap.getFieldName(typeNo, 1)).isEqualTo("publicField");
		assertThat(fieldMap.getFieldByName(typeNo, "publicField")).isEqualTo(addedField);

		addedField = fieldMap.addField(
				typeNo,
				"immutableField",
				12,
				fieldTypeNo,
				false,
				Visibility.NAMESPACE,
				Mutability.VALUE_OR_REF_IMMUTABLE,
				false,
				false);

		assertThat(addedField).isGreaterThanOrEqualTo(0);
		assertThat(fieldMap.getFieldCount(typeNo)).isEqualTo(3);
		assertThat(fieldMap.getFieldNo(typeNo, 2)).isEqualTo(addedField);
		assertThat(fieldMap.isFieldStatic(typeNo, 2)).isFalse();
		assertThat(fieldMap.getFieldVisibility(typeNo, 2)).isEqualTo(Visibility.NAMESPACE);
		assertThat(fieldMap.getFieldMutability(typeNo, 2)).isEqualTo(Mutability.VALUE_OR_REF_IMMUTABLE);
		assertThat(fieldMap.isFieldVolatile(typeNo, 2)).isFalse();
		assertThat(fieldMap.isFieldTransient(typeNo, 2)).isFalse();
		assertThat(fieldMap.getFieldType(typeNo, 2)).isEqualTo(fieldTypeNo);
		assertThat(fieldMap.getFieldName(typeNo, 2)).isEqualTo("immutableField");
		assertThat(fieldMap.getFieldByName(typeNo, "immutableField")).isEqualTo(addedField);

		addedField = fieldMap.addField(
				typeNo,
				"volatileField",
				12,
				fieldTypeNo,
				false,
				Visibility.NAMESPACE,
				Mutability.MUTABLE,
				true,
				false);

		assertThat(addedField).isGreaterThanOrEqualTo(0);
		assertThat(fieldMap.getFieldCount(typeNo)).isEqualTo(4);
		assertThat(fieldMap.getFieldNo(typeNo, 3)).isEqualTo(addedField);
		assertThat(fieldMap.isFieldStatic(typeNo, 3)).isFalse();
		assertThat(fieldMap.getFieldVisibility(typeNo, 3)).isEqualTo(Visibility.NAMESPACE);
		assertThat(fieldMap.getFieldMutability(typeNo, 3)).isEqualTo(Mutability.MUTABLE);
		assertThat(fieldMap.isFieldVolatile(typeNo, 3)).isTrue();
		assertThat(fieldMap.isFieldTransient(typeNo, 3)).isFalse();
		assertThat(fieldMap.getFieldType(typeNo, 3)).isEqualTo(fieldTypeNo);
		assertThat(fieldMap.getFieldName(typeNo, 3)).isEqualTo("volatileField");
		assertThat(fieldMap.getFieldByName(typeNo, "volatileField")).isEqualTo(addedField);

		addedField = fieldMap.addField(
				typeNo,
				"transientField",
				12,
				fieldTypeNo,
				false,
				Visibility.NAMESPACE,
				Mutability.MUTABLE,
				false,
				true);

		assertThat(addedField).isGreaterThanOrEqualTo(0);
		assertThat(fieldMap.getFieldCount(typeNo)).isEqualTo(5);
		assertThat(fieldMap.getFieldNo(typeNo, 4)).isEqualTo(addedField);
		assertThat(fieldMap.isFieldStatic(typeNo, 4)).isFalse();
		assertThat(fieldMap.getFieldVisibility(typeNo, 4)).isEqualTo(Visibility.NAMESPACE);
		assertThat(fieldMap.getFieldMutability(typeNo, 4)).isEqualTo(Mutability.MUTABLE);
		assertThat(fieldMap.isFieldVolatile(typeNo, 4)).isFalse();
		assertThat(fieldMap.isFieldTransient(typeNo, 4)).isTrue();
		assertThat(fieldMap.getFieldType(typeNo, 4)).isEqualTo(fieldTypeNo);
		assertThat(fieldMap.getFieldName(typeNo, 4)).isEqualTo("transientField");
		assertThat(fieldMap.getFieldByName(typeNo, "transientField")).isEqualTo(addedField);

		final FieldInfo fieldInfo = fieldMap.getFieldInfo(typeNo, "transientField");

		assertThat(fieldInfo.getFieldNo()).isEqualTo(addedField);
		assertThat(fieldInfo.isStatic()).isFalse();
		assertThat(fieldInfo.getVisibility()).isEqualTo(Visibility.NAMESPACE);
		assertThat(fieldInfo.getMutability()).isEqualTo(Mutability.MUTABLE);
		assertThat(fieldInfo.isVolatile()).isFalse();
		assertThat(fieldInfo.isTransient()).isTrue();
		assertThat(fieldInfo.getFieldType()).isEqualTo(fieldTypeNo);
	}

	@Test
	public void testGetFieldInfoForNonAddedName() {

        final FieldMap fieldMap = new FieldMap();

        final int typeNo = 1;

        assertThat(fieldMap.getFieldInfo(typeNo, "nonAddedField")).isNull();
	}

    @Test
    public void testGetFieldNo() {

        final FieldMap fieldMap = new FieldMap();

        final int typeNo = 1;
        final int fieldTypeNo = 123;

        final int addedField = fieldMap.addField(
                typeNo,
                "staticField",
                12,
                fieldTypeNo,
                true,
                Visibility.NAMESPACE,
                Mutability.MUTABLE,
                false,
                false);

        assertThat(addedField).isGreaterThanOrEqualTo(0);

        assertThat(fieldMap.getFieldByName(typeNo, "staticField")).isEqualTo(addedField);
    }

	@Test
	public void testGetFieldNoForNonAddedName() {

        final FieldMap fieldMap = new FieldMap();

        final int typeNo = 1;

        assertThat(fieldMap.getFieldByName(typeNo, "nonAddedField")).isNull();
	}

	@Test
	public void testMultipleFieldsWithSameNameForDifferentTypes() {

        final FieldMap fieldMap = new FieldMap();

        final int typeNo = 1;
        final int fieldTypeNo = 123;

        int addedField = fieldMap.addField(
                typeNo,
                "staticField",
                12,
                fieldTypeNo,
                true,
                Visibility.NAMESPACE,
                Mutability.MUTABLE,
                false,
                false);

        assertThat(addedField).isGreaterThanOrEqualTo(0);

        final int otherTypeNo = 2;

        addedField = fieldMap.addField(
                otherTypeNo,
                "staticField",
                12,
                fieldTypeNo,
                true,
                Visibility.NAMESPACE,
                Mutability.MUTABLE,
                false,
                false);

        assertThat(addedField).isGreaterThanOrEqualTo(0);
	}

    @Test
    public void testSameNameForSameTypeThrowsException() {

        final FieldMap fieldMap = new FieldMap();

        final int typeNo = 1;
        final int fieldTypeNo = 123;

        int addedField = fieldMap.addField(
                typeNo,
                "staticField",
                12,
                fieldTypeNo,
                true,
                Visibility.NAMESPACE,
                Mutability.MUTABLE,
                false,
                false);

        assertThat(addedField).isGreaterThanOrEqualTo(0);

        try {
            addedField = fieldMap.addField(
                typeNo,
                "staticField",
                12,
                fieldTypeNo,
                true,
                Visibility.NAMESPACE,
                Mutability.MUTABLE,
                false,
                false);

            fail("Expected exception");
        }
        catch (IllegalArgumentException ex) {

        }
    }

}
