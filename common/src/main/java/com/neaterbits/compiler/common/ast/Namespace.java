package com.neaterbits.compiler.common.ast;

import java.util.Arrays;

import com.neaterbits.compiler.common.Context;

public class Namespace extends CompilationCode {

	private final String name;
	private final String [] parts;
	private final CompilationCodeLines lines;
	
	public Namespace(Context context, String name, String [] parts, CompilationCodeLines lines) {
		super(context);
		
		this.name = name;
		this.parts = Arrays.copyOf(parts, parts.length);
		this.lines = lines;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onNamespace(this, param);

	}

	public final String getName() {
		return name;
	}

	public final String [] getParts() {
		return Arrays.copyOf(parts, parts.length);
	}

	public final CompilationCodeLines getLines() {
		return lines;
	}
}
