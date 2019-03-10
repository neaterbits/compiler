package com.neaterbits.compiler.ast.expression;

import java.util.List;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.expression.literal.Primary;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.util.Context;

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
	public BaseType getType() {
		return primaries.getLast().getType();
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
