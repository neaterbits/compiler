package com.neaterbits.exe.codegen.x86;

import com.neaterbits.exe.codegen.common.CPUArchitecture;
import com.neaterbits.exe.codegen.common.OperandSize;
import com.neaterbits.exe.codegen.common.Operation;

public class X86CPUArchitecture implements CPUArchitecture {

	@Override
	public int getMaxGeneralPurposeRegisterOffset() {
		return 15;
	}

	@Override
	public int getNumberOfUsableGeneralPurposeRegisters() {
		return 13;
	}
	
	@Override
	public boolean isMemoryAddressable(int offset) {
		return true;
	}

	@Override
	public boolean isRegisterAddressableForSize(int offset, int size) {

		final boolean addressable;
		
		switch (offset) {
		
		case X86RegisterOffsets.AX:
		case X86RegisterOffsets.CX:
		case X86RegisterOffsets.DX:
		case X86RegisterOffsets.BX:
			addressable = true;
			break;
			
		case X86RegisterOffsets.BP:
		case X86RegisterOffsets.SP:
			addressable = false;
			break;
			
		case X86RegisterOffsets.SI:
		case X86RegisterOffsets.DI:
			addressable = size != OperandSize.BYTE;
			break;
			
		case X86RegisterOffsets.R8:
		case X86RegisterOffsets.R9:
		case X86RegisterOffsets.R10:
		case X86RegisterOffsets.R11:
		case X86RegisterOffsets.R12:
		case X86RegisterOffsets.R13:
		case X86RegisterOffsets.R14:
		case X86RegisterOffsets.R15:
			addressable = true;
			break;

		default:
			throw new UnsupportedOperationException();
		}
		
		return addressable;
	}

	@Override
	public int getNumberOfRegistersForOperation(Operation operation) {
		throw new UnsupportedOperationException("TODO");
	}
}
