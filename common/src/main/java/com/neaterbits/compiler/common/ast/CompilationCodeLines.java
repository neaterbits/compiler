package com.neaterbits.compiler.common.ast;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.list.ASTList;

public class CompilationCodeLines extends BaseASTElement {
	
	private final ASTList<CompilationCode> code;

	public CompilationCodeLines(Context context, List<CompilationCode> code) {
		super(context);
		
		Objects.requireNonNull(code);
		
		this.code = makeList(code);
	}

	public final ASTList<CompilationCode> getCode() {
		return code;
	}
}
