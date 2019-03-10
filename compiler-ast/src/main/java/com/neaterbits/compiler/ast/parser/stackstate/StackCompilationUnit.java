package com.neaterbits.compiler.ast.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.CompilationCode;
import com.neaterbits.compiler.ast.Import;
import com.neaterbits.compiler.ast.parser.ListStackEntry;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackCompilationUnit extends ListStackEntry<CompilationCode> {

	private final List<Import> imports;

	public StackCompilationUnit(ParseLogger parseLogger) {
		super(parseLogger);

		this.imports = new ArrayList<>();
	}
	
	public void addImport(Import importStatement) {
		Objects.requireNonNull(importStatement);
	
		imports.add(importStatement);
	}

	public List<Import> getImports() {
		return imports;
	}
}
