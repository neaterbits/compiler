package com.neaterbits.compiler.common.ast;

import java.util.Collections;
import java.util.List;

import com.neaterbits.compiler.common.Context;

public class CompilationUnit extends CompilationCodeLines {

	private final List<Import> imports;
	
	public CompilationUnit(Context context, List<Import> imports, List<CompilationCode> code) {
		super(context, code);
		
		this.imports = Collections.unmodifiableList(imports);
	}

	public List<Import> getImports() {
		return imports;
	}
}
