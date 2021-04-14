package dev.nimbler.exe.vm.bytecode.loader;

import dev.nimbler.language.bytecode.common.ClassBytecode;

public interface LoadedClasses {

	ClassBytecode getBytecode(int type);
	
}
