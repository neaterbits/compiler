package com.neaterbits.compiler.util.parse.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.util.parse.ParseLogger;
import com.neaterbits.compiler.util.parse.listener.stackbased.state.base.ListStackEntry;

public final class StackCompilationUnit<COMPILATION_CODE, IMPORT> extends ListStackEntry<COMPILATION_CODE> {

	private final List<IMPORT> imports;

	public StackCompilationUnit(ParseLogger parseLogger) {
		super(parseLogger);

		this.imports = new ArrayList<>();
	}
	
	public void addImport(IMPORT importStatement) {
		Objects.requireNonNull(importStatement);
	
		imports.add(importStatement);
	}

	public List<IMPORT> getImports() {
		return imports;
	}
}
