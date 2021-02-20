package com.neaterbits.compiler.common.ast;

import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.list.ASTList;

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
