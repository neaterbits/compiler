package com.neaterbits.compiler.bytecode.common.loader;

import com.neaterbits.compiler.bytecode.common.ClassBytecode;

public interface LoadedClasses {

	ClassBytecode getBytecode(int type);
	
}
