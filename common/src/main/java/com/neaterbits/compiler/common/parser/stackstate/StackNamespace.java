package com.neaterbits.compiler.common.parser.stackstate;

import java.util.Arrays;

import com.neaterbits.compiler.common.ast.CompilationCode;
import com.neaterbits.compiler.common.parser.NamedListStackEntry;

public final class StackNamespace extends NamedListStackEntry<CompilationCode> {

	private final String [] parts;
	
	public StackNamespace(String name, String [] parts) {
		super(name);

		this.parts = Arrays.copyOf(parts, parts.length);
	}

	public String[] getParts() {
		return parts;
	}
}
