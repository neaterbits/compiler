package com.neaterbits.exe.codegen.common;

public class StackAndRegisterAllocator {

	@SuppressWarnings("unused")
    private final int maxNumberOfRegisterPassedParameters;
	@SuppressWarnings("unused")
    private final CPUArchitecture cpuArchitecture;
	
	public StackAndRegisterAllocator(int maxNumberOfRegisterPassedParameters, CPUArchitecture cpuArchitecture) {
		this.maxNumberOfRegisterPassedParameters = maxNumberOfRegisterPassedParameters;
		this.cpuArchitecture = cpuArchitecture;
		
		// Some registers available for calculations
		if (cpuArchitecture.getNumberOfUsableGeneralPurposeRegisters() - maxNumberOfRegisterPassedParameters < 3) {
			throw new IllegalStateException();
		}
	}

	public void allocateStackAndRegisters(AllocatedStackAndRegisters result) {
		
	}
}
