package com.neaterbits.compiler.ast.objects.expression;

import java.util.List;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.util.parse.context.Context;

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
