package com.neaterbits.exe.vm.bytecode.executor;

import java.util.Objects;

import com.neaterbits.runtime._native.NativeMemory;

final class CompiledMethod {

	private final NativeMemory nativeCode;

	CompiledMethod(NativeMemory nativeCode) {
		
		Objects.requireNonNull(nativeCode);
		
		this.nativeCode = nativeCode;
	}

	NativeMemory getNativeCode() {
		return nativeCode;
	}
}
