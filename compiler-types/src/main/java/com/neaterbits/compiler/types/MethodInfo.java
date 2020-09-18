package com.neaterbits.compiler.types;

import java.util.Objects;

public final class MethodInfo {

	private final int methodNo;
	private final MethodVariant methodVariant;

	public MethodInfo(int methodNo, MethodVariant methodVariant) {

		Objects.requireNonNull(methodVariant);

		this.methodNo = methodNo;
		this.methodVariant = methodVariant;
	}

	public int getMethodNo() {
		return methodNo;
	}

	public MethodVariant getMethodVariant() {
		return methodVariant;
	}
}
