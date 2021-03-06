package dev.nimbler.language.codemap;

import dev.nimbler.language.common.types.FieldModifiers;
import dev.nimbler.language.common.types.Mutability;
import dev.nimbler.language.common.types.Visibility;

public class FieldInfo extends FieldModifiers {

	private final int fieldNo;
	private final int fieldType;
	
	public FieldInfo(int fieldNo, int fieldType, boolean isStatic, Visibility visibility, Mutability mutability, boolean isVolatile,
			boolean isTransient) {
		super(isStatic, visibility, mutability, isVolatile, isTransient);

		this.fieldNo = fieldNo;
		this.fieldType = fieldType;
	}

	public final int getFieldNo() {
		return fieldNo;
	}

	public final int getFieldType() {
		return fieldType;
	}
}
