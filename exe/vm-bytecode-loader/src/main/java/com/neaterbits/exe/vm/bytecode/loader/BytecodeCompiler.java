package com.neaterbits.exe.vm.bytecode.loader;

import com.neaterbits.language.bytecode.common.ClassBytecode;
import com.neaterbits.language.codemap.VTableScratchArea;
import com.neaterbits.language.common.types.MethodVariant;

public interface BytecodeCompiler<CLASS, METHOD> {

	CLASS initializeClass(int type, ClassBytecode classBytecode, VTableScratchArea scratchArea);
	
	METHOD compileMethod(int type, int methodIdx, MethodVariant methodVariant, byte [] bytecode);
}
