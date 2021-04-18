package dev.nimbler.compiler.ast.objects.expression;

import java.util.List;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.types.ParseTreeElement;

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
