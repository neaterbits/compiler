package dev.nimbler.exe.codegen.common;

abstract class InstructionToLongMap {

	private int [] scratchBufferIndexByInstructionOffset;
	
	long [] scratchBuffer;
	
	private int scratchBufferAllocIndex;

	InstructionToLongMap(int numInstructionOffsets) {
		this.scratchBufferIndexByInstructionOffset = new int[numInstructionOffsets];
		
		this.scratchBuffer = new long[numInstructionOffsets * 3];
		this.scratchBufferAllocIndex = 0;
	}
	
	final int getIndex(int instructionOffset) {
		return scratchBufferIndexByInstructionOffset[instructionOffset] + 1;
	}
	
	
	final int allocFromScratchBuffer(int instructionOffset, int numLongs) {
		
		if (scratchBufferIndexByInstructionOffset[instructionOffset] != 0) {
			throw new IllegalStateException();
		}
		
		final int index = scratchBufferAllocIndex;
		
		scratchBufferAllocIndex += numLongs + 1;
		
		scratchBufferIndexByInstructionOffset[instructionOffset] = index;
		
		scratchBuffer[index + 1] = numLongs;
		
		return index + 1;
	}
}
