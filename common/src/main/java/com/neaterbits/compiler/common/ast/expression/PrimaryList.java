package com.neaterbits.compiler.common.ast.expression;

import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.expression.literal.Primary;
import com.neaterbits.compiler.common.ast.list.ASTList;

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
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onPrimaryList(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(primaries, recurseMode, iterator);
	}
}
