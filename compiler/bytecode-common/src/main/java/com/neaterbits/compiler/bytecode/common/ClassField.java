package com.neaterbits.compiler.bytecode.common;

import java.util.Objects;

import com.neaterbits.compiler.util.FieldType;

public final class ClassField {

	private final String name;
	private final FieldType fieldType;
	
	public ClassField(String name, FieldType fieldType) {
		
		Objects.requireNonNull(name);
		Objects.requireNonNull(fieldType);
		
		this.name = name;
		this.fieldType = fieldType;
	}

	public String getName() {
		return name;
	}

	public FieldType getFieldType() {
		return fieldType;
	}
}
