package dev.nimbler.language.bytecode.common;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import dev.nimbler.language.common.types.FieldType;
import dev.nimbler.language.common.types.MethodVariant;

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
