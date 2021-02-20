package com.neaterbits.compiler.common.ast.expression;

import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.list.ASTList;

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
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(list, recurseMode, iterator);
	}
}
