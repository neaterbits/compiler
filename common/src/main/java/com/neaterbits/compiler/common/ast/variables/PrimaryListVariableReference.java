package com.neaterbits.compiler.common.ast.variables;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
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
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		doIterate(list, recurseMode, visitor);
	}
}
