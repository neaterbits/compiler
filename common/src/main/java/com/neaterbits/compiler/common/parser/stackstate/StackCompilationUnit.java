package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.ast.CompilationCode;
import com.neaterbits.compiler.common.ast.Import;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ListStackEntry;

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
