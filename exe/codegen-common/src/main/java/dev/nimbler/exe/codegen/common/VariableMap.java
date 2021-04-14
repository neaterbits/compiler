package dev.nimbler.exe.codegen.common;

final class VariableMap extends InstructionToLongMap {

	
	VariableMap(int numInstructionOffsets) {
		super(numInstructionOffsets);
	}
	
	private int allocScratchBuffer(int instructionOffset, Operation operation, int numExtra) {

		final int index = super.allocFromScratchBuffer(instructionOffset, 1 + numExtra);
		
		scratchBuffer[index + 0] = operation.ordinal();
		
		return index;
	}
	
	int startVariable(int instructionOffset, Operation operation) {
		
		final int index = allocScratchBuffer(instructionOffset, operation, 0);

		return index;
	}
	
	int startVariable(int instructionOffset, int replaceVariable) {
		throw new UnsupportedOperationException("TODO");
	}

	int startVariable(int instructionOffset, int replaceVariable1, int replaceVariable2) {
		throw new UnsupportedOperationException("TODO");
	}

	int startVariable(int instructionOffset, int replaceVariable1, int replaceVariable2, int replaceVariable3) {
		throw new UnsupportedOperationException("TODO");
	}

	void endVariable(int instructionOffset, int variable) {
		throw new UnsupportedOperationException("TODO");
	}
	
}
