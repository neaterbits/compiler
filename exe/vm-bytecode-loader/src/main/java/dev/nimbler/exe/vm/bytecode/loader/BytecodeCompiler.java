package dev.nimbler.exe.vm.bytecode.loader;

import dev.nimbler.language.bytecode.common.ClassBytecode;
import dev.nimbler.language.codemap.VTableScratchArea;
import dev.nimbler.language.common.types.MethodVariant;

public interface BytecodeCompiler<CLASS, METHOD> {

	CLASS initializeClass(int type, ClassBytecode classBytecode, VTableScratchArea scratchArea);
	
	METHOD compileMethod(int type, int methodIdx, MethodVariant methodVariant, byte [] bytecode);
}
