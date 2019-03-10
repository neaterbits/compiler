package com.neaterbits.compiler.ast;

import java.util.List;

import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.util.Context;

public class CompilationUnit extends CompilationCodeLines {

	private final ASTList<Import> imports;
	
	public CompilationUnit(Context context, List<Import> imports, List<CompilationCode> code) {
		super(context, code);
		
		this.imports = makeList(imports);
	}

	public ASTList<Import> getImports() {
		return imports;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(imports, recurseMode, iterator);
		
		super.doRecurse(recurseMode, iterator);
	}
}
