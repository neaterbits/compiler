package com.neaterbits.exe.vm.bytecode.loader;

import java.util.Arrays;
import java.util.Objects;

import com.neaterbits.language.bytecode.common.ClassBytecode;

final class LoadedClassesCache implements LoadedClasses {

	private ClassBytecode [] classes;

	LoadedClassesCache() {
		this.classes = new ClassBytecode[1000];
	}
	
	synchronized void addClass(int type, ClassBytecode bytecode) {
		
		Objects.requireNonNull(bytecode);
		
		if (type >= classes.length) {
			this.classes = Arrays.copyOf(classes, (type * 3));
		}

		if (classes[type] != null) {
			throw new IllegalStateException();
		}
		
		classes[type] = bytecode;
	}

	@Override
	public synchronized ClassBytecode getBytecode(int type) {
		return classes[type];
	}
}
