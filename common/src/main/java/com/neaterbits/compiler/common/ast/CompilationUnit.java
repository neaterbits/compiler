package com.neaterbits.compiler.common.ast;

import java.util.List;

import com.neaterbits.compiler.common.Context;

public class CompilationUnit extends CompilationCodeLines {

	public CompilationUnit(Context context, List<CompilationCode> code) {
		super(context, code);
	}
}
