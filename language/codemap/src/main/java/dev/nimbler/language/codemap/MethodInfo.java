package dev.nimbler.language.codemap;

import java.util.Objects;

import dev.nimbler.language.common.types.MethodVariant;

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
