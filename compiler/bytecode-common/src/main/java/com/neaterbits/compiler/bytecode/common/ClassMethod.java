package com.neaterbits.compiler.bytecode.common;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.types.MethodVariant;
import com.neaterbits.compiler.util.FieldType;

public final class ClassMethod {

	private final String name;
	private final FieldType returnType;
	private final List<FieldType> parameterTypes;
	private final MethodVariant variant;

	public ClassMethod(String name, FieldType returnType, List<FieldType> parameterTypes, MethodVariant variant) {
		
		Objects.requireNonNull(name);
		Objects.requireNonNull(returnType);
		Objects.requireNonNull(parameterTypes);
		Objects.requireNonNull(variant);
		
		this.name = name;
		this.returnType = returnType;
		this.parameterTypes = Collections.unmodifiableList(parameterTypes);
		this.variant = variant;
	}

	public String getName() {
		return name;
	}

	public FieldType getReturnType() {
		return returnType;
	}

	public List<FieldType> getParameterTypes() {
		return parameterTypes;
	}
	
	public MethodVariant getVariant() {
		return variant;
	}
	
	public byte [] getBytecode() {
		throw new UnsupportedOperationException();
	}

	public int getMaxOperandStack() {
		throw new UnsupportedOperationException();
	}
	
	public int getMaxLocals() {
		throw new UnsupportedOperationException();
	}
}
