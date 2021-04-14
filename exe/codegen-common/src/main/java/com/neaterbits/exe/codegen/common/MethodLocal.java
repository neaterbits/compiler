package com.neaterbits.exe.codegen.common;

public class MethodLocal extends AllocatedStackAndRegisters {

	
	// parameters passed to method
	private final int numParameters;

	// variables that are truly local
	private final int numBytecodeLocalVariables;
	
	// computed local variables, after unwinding operand stack etc
	private final int numComputedLocalVariables;
	
	// map from bytecode locals to register/stack allocated locals
	// private final long [] bytecodeLocalToLocals;


	public MethodLocal(
			int numInstructionOffsets,
			int numParameters,
			int numBytecodeLocalVariables,
			int numComputedLocalVariables /* ,
			long[] bytecodeLocalToLocals */) {
		
		super(numInstructionOffsets);

		this.numParameters = numParameters;
		this.numBytecodeLocalVariables = numBytecodeLocalVariables;
		this.numComputedLocalVariables = numComputedLocalVariables;
		// this.bytecodeLocalToLocals = bytecodeLocalToLocals;
	}
	
	/*
	public int getRegisterNo(int bytecodeVariable) {
		final long var = bytecodeLocalToLocals[bytecodeVariable];
	
		return ((var & STACK) != 0) ? -1 : (int)(var & REGISTER_MASK);
	}
	*/


	int getNumParameters() {
		return numParameters;
	}


	int getNumBytecodeLocalVariables() {
		return numBytecodeLocalVariables;
	}


	int getNumComputedLocalVariables() {
		return numComputedLocalVariables;
	}
}
