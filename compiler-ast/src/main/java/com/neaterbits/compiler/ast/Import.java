package com.neaterbits.compiler.ast;

import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.TypeImportVisitor;

public final class Import extends BaseASTElement {

	private final ASTSingle<Keyword> keyword;
	private final ASTSingle<ImportName> _package;

	public Import(Context context, Keyword keyword, ImportName _package) {
		super(context);

		this.keyword = makeSingle(keyword);
		this._package = makeSingle(_package);
	}

	public Keyword getKeyword() {
		return keyword.get();
	}
	
	public ImportName getPackage() {
		return _package.get();
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(keyword, recurseMode, iterator);
		doIterate(_package, recurseMode, iterator);
		
	}

	public void visit(TypeImportVisitor visitor) {
		_package.get().visit(visitor);
	}
}
