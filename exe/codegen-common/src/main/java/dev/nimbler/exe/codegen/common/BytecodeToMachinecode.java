package dev.nimbler.exe.codegen.common;

import java.util.Objects;

import dev.nimbler.language.bytecode.common.BytecodeInstructions;

public abstract class BytecodeToMachinecode implements BytecodeInstructions {

	protected byte [] buffer;
	protected int count;

	protected AllocatedStackAndRegisters allocatedStackAndRegisters;
	
	protected final void add(int bytes) {
		count += bytes;
	}

	final void setAllocatedStackAndRegisters(AllocatedStackAndRegisters allocatedStackAndRegisters) {
		
		Objects.requireNonNull(allocatedStackAndRegisters);
		
		this.allocatedStackAndRegisters = allocatedStackAndRegisters;
	}
}
