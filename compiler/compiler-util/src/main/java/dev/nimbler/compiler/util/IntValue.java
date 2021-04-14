package dev.nimbler.compiler.util;

public final class IntValue {

	private int value;

	public IntValue(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int increment() {
		return value ++;
	}
}
