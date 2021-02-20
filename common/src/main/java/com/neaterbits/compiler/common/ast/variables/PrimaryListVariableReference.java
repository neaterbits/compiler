package com.neaterbits.compiler.common.ast.variables;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.expression.PrimaryList;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public final class PrimaryListVariableReference extends VariableReference {

	private final ASTSingle<PrimaryList> list;

	public PrimaryListVariableReference(Context context, PrimaryList list) {
		super(context);
		
		Objects.requireNonNull(list);
		
		this.list = makeSingle(list);
	}

	public PrimaryList getList() {
		return list.get();
	}

	@Override
	public <T, R> R visit(VariableReferenceVisitor<T, R> visitor, T param) {
		return visitor.onPrimaryList(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(list, recurseMode, iterator);
	}
}
