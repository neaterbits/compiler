package com.neaterbits.compiler.bytecode.common.loader;

import com.neaterbits.compiler.bytecode.common.ClassBytecode;
import com.neaterbits.compiler.codemap.VTableScratchArea;
import com.neaterbits.compiler.types.MethodVariant;

public interface BytecodeCompiler<CLASS, METHOD> {

	CLASS initializeClass(int type, ClassBytecode classBytecode, VTableScratchArea scratchArea);
	
	METHOD compileMethod(int type, int methodIdx, MethodVariant methodVariant, byte [] bytecode);
}
