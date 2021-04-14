package com.neaterbits.compiler.ast.objects.variables;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.expression.PrimaryList;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.util.parse.context.Context;

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
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.PRIMARY_LIST_VARIABLE_REFERENCE;
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
