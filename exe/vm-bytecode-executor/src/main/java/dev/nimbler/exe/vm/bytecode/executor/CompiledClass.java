package dev.nimbler.exe.vm.bytecode.executor;

import java.util.Objects;

import dev.nimbler.runtime._native.NativeMemory;

final class CompiledClass {

	private final NativeMemory vtable;
	private final NativeMemory classObject;
	private final NativeMemory staticFields;
	
	CompiledClass(NativeMemory vtable, NativeMemory classObject, NativeMemory staticFields) {
		
		Objects.requireNonNull(vtable);
		Objects.requireNonNull(classObject);
		Objects.requireNonNull(staticFields);
		
		this.vtable = vtable;
		this.classObject = classObject;
		this.staticFields = staticFields;
	}

	NativeMemory getVtable() {
		return vtable;
	}

	NativeMemory getClassObject() {
		return classObject;
	}

	NativeMemory getStaticFields() {
		return staticFields;
	}
}
