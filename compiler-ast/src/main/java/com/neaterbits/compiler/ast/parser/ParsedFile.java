package com.neaterbits.compiler.ast.parser;

import java.util.Collections;
import java.util.List;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.BasePlaceholderASTElement;
import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.util.parse.CompileError;

public final class ParsedFile extends BasePlaceholderASTElement {

	private final SourceFile file;
	private final List<CompileError> errors;
	private final String log;
	private final ASTSingle<CompilationUnit> parsed;

	public ParsedFile(SourceFile file, List<CompileError> errors, String log, CompilationUnit parsed) {
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
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(parsed, recurseMode, iterator);
	}
}
