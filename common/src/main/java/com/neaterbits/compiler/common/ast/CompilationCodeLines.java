package com.neaterbits.compiler.common.ast;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;

public class CompilationCodeLines extends BaseASTElement {
	
	private final List<CompilationCode> code;

	public CompilationCodeLines(Context context, List<CompilationCode> code) {
		super(context);
		
		Objects.requireNonNull(code);
		
		this.code = code;
	}
}
