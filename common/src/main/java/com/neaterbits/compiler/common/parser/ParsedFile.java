package com.neaterbits.compiler.common.parser;

import java.util.Collections;
import java.util.List;

import com.neaterbits.compiler.common.CompileError;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.CompilationUnit;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public final class ParsedFile extends BaseASTElement {

	private final SourceFile file;
	private final List<CompileError> errors;
	private final String log;
	private final ASTSingle<CompilationUnit> parsed;

	public ParsedFile(SourceFile file, List<CompileError> errors, String log, CompilationUnit parsed) {
		super(null);
		
		this.file = file;
		this.errors = Collections.unmodifiableList(errors);
		this.log = log;
		this.parsed = makeSingle(parsed);
	}

	public SourceFile getFile() {
		return file;
	}

	public List<CompileError> getErrors() {
		return errors;
	}

	public String getLog() {
		return log;
	}

	public CompilationUnit getParsed() {
		return parsed.get();
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		doIterate(parsed, recurseMode, visitor);
	}
}
