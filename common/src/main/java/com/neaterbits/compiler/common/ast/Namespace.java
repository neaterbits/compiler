package com.neaterbits.compiler.common.ast;

import com.neaterbits.compiler.common.Context;

public class Namespace extends CompilationCode {

	private final String name;
	private final CompilationCodeLines lines;
	
	public Namespace(Context context, String name, CompilationCodeLines lines) {
		super(context);
		
		this.name = name;
		this.lines = lines;
	}
}
