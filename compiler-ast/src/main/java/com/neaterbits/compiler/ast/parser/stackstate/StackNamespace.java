package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.Arrays;

import com.neaterbits.compiler.ast.CompilationCode;
import com.neaterbits.compiler.ast.parser.NamedListStackEntry;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackNamespace extends NamedListStackEntry<CompilationCode> {

	private final String [] parts;
	
	public StackNamespace(ParseLogger parseLogger, String name, String [] parts) {
		super(parseLogger, name);

		this.parts = Arrays.copyOf(parts, parts.length);
	}

	public String[] getParts() {
		return parts;
	}
}
