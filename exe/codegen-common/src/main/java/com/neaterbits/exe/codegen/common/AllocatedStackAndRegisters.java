package com.neaterbits.exe.codegen.common;

public class AllocatedStackAndRegisters extends InstructionToLongMap {

	private static final long REDUNDANT = 1L << 63;
	
	private static final long COUNTS_MASK = ~REDUNDANT;


	AllocatedStackAndRegisters(int numInstructionOffsets) {
		super(numInstructionOffsets);
	}
	
	
	public boolean isRedundantInstruction(int instructionOffset) {

		final int index = getIndex(instructionOffset);
		
		return (scratchBuffer[index] & REDUNDANT) != 0L;
	}

	
	public long getSrcOperandEncoded(int instructionOffset, int operandNo) {
		final int index = getIndex(instructionOffset);

		final int numSourceOperands = getNumSourceOperands(instructionOffset);
		if (operandNo >= numSourceOperands) {
			throw new IllegalStateException();
		}

		final long encoded = scratchBuffer[index + 1 + operandNo];
	
		return encoded;
	}

	public long getDstOperandEncoded(int instructionOffset, int operandNo) {
		final int index = getIndex(instructionOffset);
		
		final int numSourceOperands = getNumSourceOperands(instructionOffset);
		final int numDestinationOperands = getNumDestinationOperands(instructionOffset);
		
		if (operandNo >= numDestinationOperands) {
			throw new IllegalStateException();
		}

		final long encoded = scratchBuffer[index + 1 + numSourceOperands + operandNo];
	
		return encoded;
	}

	public int getNumOperands(int instructionOffset) {
		final int index = getIndex(instructionOffset);
		
		final long val = scratchBuffer[index];
	
		return getNumSourceOperandsFromVal(val) + getNumDestinationOperandsFromVal(val);
	}

	private static int getNumSourceOperandsFromVal(long val) {
		return (int)((val & COUNTS_MASK) >>> 32);
	}

	private static int getNumDestinationOperandsFromVal(long val) {
		return (int)(val & 0x00000000FFFFFFFFL);
	}
	
	private int getNumSourceOperands(int index) {
		return getNumSourceOperandsFromVal(scratchBuffer[index]);
	}

	private int getNumDestinationOperands(int index) {
		return getNumDestinationOperandsFromVal(scratchBuffer[index]);
	}
	
	void storeSourceOperand(int instructionOffset, long sourceOperand) {
		
		final int index = getIndex(instructionOffset);
		
		if (getNumSourceOperands(index) != 1) {
			throw new IllegalStateException();
		}
		
		scratchBuffer[index + 1] = sourceOperand;
	}

	void storeSourceOperands(int instructionOffset, long sourceOperand1, long sourceOperand2) {
		
		final int index = getIndex(instructionOffset);
		
		if (getNumSourceOperands(index) != 2) {
			throw new IllegalStateException();
		}

		scratchBuffer[index + 1] = sourceOperand1;
		scratchBuffer[index + 2] = sourceOperand2;
	}

	void storeSourceOperands(int instructionOffset, long sourceOperand1, long sourceOperand2, long sourceOperand3) {
		
		final int index = getIndex(instructionOffset);
		
		if (getNumSourceOperands(index) != 3) {
			throw new IllegalStateException();
		}

		scratchBuffer[index + 1] = sourceOperand1;
		scratchBuffer[index + 2] = sourceOperand2;
		scratchBuffer[index + 3] = sourceOperand3;
	}

	void storeDestinationOperand(int instructionOffset, long destinationOperand) {
		
		final int index = getIndex(instructionOffset);
		
		final int numSourceOperands = getNumSourceOperands(index);
		
		if (getNumDestinationOperands(index) != 1) {
			throw new IllegalStateException();
		}
		
		scratchBuffer[index + 1 + numSourceOperands] = destinationOperand;
	}
}
