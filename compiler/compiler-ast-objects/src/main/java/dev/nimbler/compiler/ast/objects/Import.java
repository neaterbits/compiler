package dev.nimbler.compiler.ast.objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.imports.TypeImportVisitor;

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
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.IMPORT;
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
