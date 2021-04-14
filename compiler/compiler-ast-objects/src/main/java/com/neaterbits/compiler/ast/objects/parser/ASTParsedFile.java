package com.neaterbits.compiler.ast.objects.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.CompilationUnit;
import com.neaterbits.compiler.util.FileSpec;
import com.neaterbits.compiler.util.parse.CompileError;
import com.neaterbits.compiler.util.parse.ParsedFile;

public final class ASTParsedFile implements ParsedFile {

	private final FileSpec file;
	private final List<CompileError> errors;
	private final String log;
	private final CompilationUnit parsed;

	public ASTParsedFile(FileSpec file, List<CompileError> errors, String log, CompilationUnit parsed) {
		this.file = file;
		this.errors = Collections.unmodifiableList(errors);
		this.log = log;
		this.parsed = parsed;
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
		return parsed;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public <AST_ELEMENT> List<AST_ELEMENT> getASTElements(Class<AST_ELEMENT> type) {

		final List<BaseASTElement> elements = new ArrayList<>();
		
		parsed.iterateNodeFirst(e -> elements.add(e));
		
		return (List)elements;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <COMPILATION_UNIT> COMPILATION_UNIT getCompilationUnit() {
		return (COMPILATION_UNIT)parsed;
	}

    @Override
    public String toString() {
        return "ASTParsedFile [file=" + file + ", errors=" + errors + ", log=" + log + ", parsed=" + parsed + "]";
    }
}
