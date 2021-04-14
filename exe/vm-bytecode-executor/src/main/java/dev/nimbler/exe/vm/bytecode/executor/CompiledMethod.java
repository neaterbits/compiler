package dev.nimbler.exe.vm.bytecode.executor;

import java.util.Objects;

import dev.nimbler.runtime._native.NativeMemory;

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
