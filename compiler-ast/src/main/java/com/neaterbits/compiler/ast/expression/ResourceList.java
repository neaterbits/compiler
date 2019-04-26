package com.neaterbits.compiler.ast.expression;

import java.util.List;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class ResourceList extends BaseASTElement {

	private final ASTList<Resource> list;

	public ResourceList(Context context, List<Resource> list) {
		super(context);

		this.list = makeList(list);
	}

	public ASTList<Resource> getList() {
		return list;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.RESOURCES_LIST;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(list, recurseMode, iterator);
	}
}
