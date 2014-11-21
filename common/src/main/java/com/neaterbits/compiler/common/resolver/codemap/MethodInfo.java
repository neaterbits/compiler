package com.neaterbits.compiler.common.resolver.codemap;

import java.util.Objects;

public class MethodInfo {

	private final int methodNo;
	private final MethodVariant methodVariant;

	MethodInfo(int methodNo, MethodVariant methodVariant) {

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
