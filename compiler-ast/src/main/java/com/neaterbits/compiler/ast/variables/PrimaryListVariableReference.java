package com.neaterbits.compiler.ast.variables;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.expression.PrimaryList;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

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
	public TypeReference getType() {
		return list.get().getType();
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
