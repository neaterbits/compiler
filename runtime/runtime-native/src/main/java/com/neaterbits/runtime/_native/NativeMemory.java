package com.neaterbits.runtime._native;

public final class NativeMemory {

	private final long address;

	NativeMemory(long address) {
		this.address = address;
	}

	public long getAddress() {
		return address;
	}
}
