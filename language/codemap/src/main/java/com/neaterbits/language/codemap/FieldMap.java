package com.neaterbits.language.codemap;

import static com.neaterbits.language.codemap.ArrayAllocation.addToSubIntArray;
import static com.neaterbits.language.codemap.ArrayAllocation.allocateArray;
import static com.neaterbits.language.codemap.ArrayAllocation.allocateIntArray;
import static com.neaterbits.language.codemap.ArrayAllocation.subIntArraySize;
import static com.neaterbits.language.codemap.ArrayAllocation.subIntArrayValue;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.language.common.types.Mutability;
import com.neaterbits.language.common.types.Visibility;

final class FieldMap {

	private final Map<String, Integer> nameToNameIndex;

	private int fieldSequenceNo;

	// Each unique field name is given a sequence number
	private int fieldNameNo;
	private String [] fieldNames;

	private int [] fieldToNameIndex;

	private int [][] fieldsByType;	// Find member fields declared in type
	private int [] fieldTypeByField; // Type of the field

	private int [] typeByField;		// Find type that has member field
	private int [] indexByField;	// Index into type by methodNo


	FieldMap() {
		this.nameToNameIndex = new HashMap<>();
	}

	int addField(
			int typeNo,
			String name,
			int indexInType,
			int fieldType,
			boolean isStatic,
			Visibility visibility,
			Mutability mutability,
			boolean isVolatile,
			boolean isTransient) {

		Objects.requireNonNull(name);

		// Allocate new method identifier
		final int fieldIndex = fieldSequenceNo ++;
		final int numFields = fieldSequenceNo;

		final int encodedField = Encode.encodeField(fieldIndex, isStatic, visibility, mutability, isVolatile, isTransient);

		this.fieldsByType = allocateIntArray(this.fieldsByType, typeNo + 1, false);
		addToSubIntArray(fieldsByType, typeNo, encodedField, 5);

        Integer nameIndex = nameToNameIndex.get(name);

        if (nameIndex == null) {
            this.fieldNames = allocateArray(fieldNames, fieldNameNo + 1, length -> new String[length]);

            nameIndex = fieldNameNo ++;

            this.fieldNames[nameIndex] = name;

            nameToNameIndex.put(name, nameIndex);
        }
        else {

            final int fieldEncoded = getFieldEncodedByNameNo(typeNo, nameIndex, name);
            final int fieldNo = Encode.decodeFieldNo(fieldEncoded);

            if (typeByField[fieldNo] == typeNo) {
                throw new IllegalArgumentException("Adding field with same name");
            }
        }

        this.fieldToNameIndex = allocateIntArray(fieldToNameIndex, fieldIndex + 1);
        fieldToNameIndex[fieldIndex] = nameIndex;

		this.typeByField = allocateIntArray(this.typeByField, numFields);
		this.typeByField[fieldIndex] = typeNo;

		this.fieldTypeByField = allocateIntArray(this.fieldTypeByField, fieldIndex + 1);
		this.fieldTypeByField[fieldIndex] = fieldType;

		this.indexByField = allocateIntArray(this.indexByField, numFields);
		this.indexByField[fieldIndex] = indexInType;

		return fieldIndex;
	}

	FieldInfo getFieldInfo(int typeNo, String fieldName) {

		final Integer fieldNoEncoded = getFieldEncodedByName(typeNo, fieldName);

		final FieldInfo fieldInfo;

		if (fieldNoEncoded != null) {

			final int fnoenc = fieldNoEncoded;

			final int fieldNo = Encode.decodeFieldNo(fnoenc);

			fieldInfo = new FieldInfo(
					fieldNo,
					fieldTypeByField[fieldNo],
					Encode.isFieldStatic(fnoenc),
					Encode.getFieldVisibility(fnoenc),
					Encode.getFieldMutability(fnoenc),
					Encode.isFieldVolatile(fnoenc),
					Encode.isFieldTransient(fnoenc));
		}
		else {
			fieldInfo = null;
		}

		return fieldInfo;
	}

	int getFieldCount(int typeNo) {
		return subIntArraySize(fieldsByType, typeNo);
	}

	int getFieldEncoded(int typeNo, int idx) {
		return subIntArrayValue(fieldsByType[typeNo], idx);
	}

	int getFieldNo(int typeNo, int idx) {
		return Encode.decodeFieldNo(getFieldEncoded(typeNo, idx));
	}

	boolean isFieldStatic(int typeNo, int idx) {
		return Encode.isFieldStatic(getFieldEncoded(typeNo, idx));
	}

	boolean isFieldVolatile(int typeNo, int idx) {
		return Encode.isFieldVolatile(getFieldEncoded(typeNo, idx));
	}

	boolean isFieldTransient(int typeNo, int idx) {
		return Encode.isFieldTransient(getFieldEncoded(typeNo, idx));
	}

	Visibility getFieldVisibility(int typeNo, int idx) {
		return Encode.getFieldVisibility(getFieldEncoded(typeNo, idx));
	}

	Mutability getFieldMutability(int typeNo, int idx) {
		return Encode.getFieldMutability(getFieldEncoded(typeNo, idx));
	}

	int getFieldType(int typeNo, int idx) {
		return fieldTypeByField[getFieldNo(typeNo, idx)];
	}

	String getFieldName(int typeNo, int idx) {
		return fieldNames[fieldToNameIndex[getFieldNo(typeNo, idx)]];
	}

	Integer getFieldByName(int typeNo, String name) {

		final Integer encoded = getFieldEncodedByName(typeNo, name);

		return encoded != null ? Encode.decodeFieldNo(encoded) : null;
	}

	private Integer getFieldEncodedByName(int typeNo, String name) {

		final Integer fieldNameNo = nameToNameIndex.get(name);

		Integer found;

		if (fieldNameNo != null) {
		    found = getFieldEncodedByNameNo(typeNo, fieldNameNo, name);
		}
		else {
			found = null;
		}

		return found;
	}

	private int getFieldEncodedByNameNo(int typeNo,  int fieldNameNo, String name) {

        int found = -1;

        final int count = getFieldCount(typeNo);

        for (int i = 0; i < count; ++ i) {
            final int fieldNoEncoded = getFieldEncoded(typeNo, i);

            final int fieldNo = Encode.decodeFieldNo(fieldNoEncoded);

            if (fieldNames[fieldToNameIndex[fieldNo]].equals(name)) {
                found = fieldNoEncoded;
                break;
            }
        }

        return found;
    }
}
