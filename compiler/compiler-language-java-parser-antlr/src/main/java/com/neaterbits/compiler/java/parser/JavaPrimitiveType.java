package com.neaterbits.compiler.java.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.neaterbits.language.common.types.ScopedName;

public enum JavaPrimitiveType {
	
	VOID("void"),
	
	BYTE("byte"),
	INT("int"),
	SHORT("short"),
	LONG("long"),
	CHAR("char"),
	
	FLOAT("float"),
	DOUBLE("double"),
	
	BOOLEAN("boolean");
	
	
	private static final Map<String, JavaPrimitiveType> byString;
	
	static {
		byString = new HashMap<>(values().length);
	
		for (JavaPrimitiveType value : values()) {
			byString.put(value.name, value);
		}
	}
	
	private final String name;

	private JavaPrimitiveType(String name) {
		this.name = name;
	}

	public static JavaPrimitiveType fromString(String string) {
		
		Objects.requireNonNull(string);
		
		return byString.get(string);
	}
	
	public ScopedName getScopedName() {
		return new ScopedName(null, name);
	}
}
