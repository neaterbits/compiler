package com.neaterbits.compiler.ast;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

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

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.COMPILATION_CODE_LINES;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
		doIterate(code, recurseMode, iterator);

	}
}
