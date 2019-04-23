package com.neaterbits.compiler.ast.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.BasePlaceholderASTElement;
import com.neaterbits.compiler.ast.CompilationUnit;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.parse.CompileError;
import com.neaterbits.compiler.util.parse.ParsedFile;

public final class ASTParsedFile extends BasePlaceholderASTElement implements ParsedFile {

	private final FileSpec file;
	private final List<CompileError> errors;
	private final String log;
	private final ASTSingle<CompilationUnit> parsed;

	public ASTParsedFile(FileSpec file, List<CompileError> errors, String log, CompilationUnit parsed) {
		this.file = file;
		this.errors = Collections.unmodifiableList(errors);
		this.log = log;
		this.parsed = makeSingle(parsed);
	}

	@Override
	public FileSpec getFileSpec() {
		return file;
	}

	@Override
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

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <AST_ELEMENT> List<AST_ELEMENT> getASTElements(Class<AST_ELEMENT> type) {

		final List<BaseASTElement> elements = new ArrayList<>();
		
		parsed.get().iterateNodeFirst(e -> elements.add(e));
		
		return (List)elements;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <COMPILATION_UNIT> COMPILATION_UNIT getCompilationUnit() {
		return (COMPILATION_UNIT)parsed.get();
	}
}
