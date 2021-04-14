package dev.nimbler.compiler.ast.objects.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.ast.objects.CompilationUnit;
import dev.nimbler.compiler.util.FileSpec;
import dev.nimbler.compiler.util.parse.CompileError;
import dev.nimbler.compiler.util.parse.ParsedFile;

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
