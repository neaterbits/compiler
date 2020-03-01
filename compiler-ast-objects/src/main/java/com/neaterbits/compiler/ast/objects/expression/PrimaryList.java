package com.neaterbits.compiler.ast.objects.expression;

import java.util.List;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.expression.literal.Primary;
import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class PrimaryList extends Primary {

	private final ASTList<Primary> primaries;

	public PrimaryList(Context context, List<Primary> primaries) {
		super(context);
		
		if (primaries.size() < 2) {
			throw new IllegalArgumentException("No list required");
		}

		this.primaries = makeList(primaries);
	}

	public ASTList<Primary> getPrimaries() {
		return primaries;
	}
	
	@Override
	public TypeReference getType() {
		return primaries.getLast().getType();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.PRIMARY_LIST;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onPrimaryList(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(primaries, recurseMode, iterator);
	}
}
