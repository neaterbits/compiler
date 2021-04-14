package com.neaterbits.exe.vm.bytecode.loader;

import com.neaterbits.language.bytecode.common.ClassBytecode;

public interface LoadedClasses {

	ClassBytecode getBytecode(int type);
	
}
