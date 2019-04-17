package com.neaterbits.compiler.util.passes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class CompilerBuilder<INPUT> {

	private final List<Pass<?, ?>> passes;
	
	public CompilerBuilder() {
		this.passes = new ArrayList<>();
	}
	
	public <OUTPUT> CompilerBuilderIntermediate<OUTPUT, INPUT> addPass(Pass<INPUT, OUTPUT> pass) {

		Objects.requireNonNull(pass);
		
		passes.add(pass);
		
		return new CompilerBuilderIntermediate<>(passes);
	}
}
