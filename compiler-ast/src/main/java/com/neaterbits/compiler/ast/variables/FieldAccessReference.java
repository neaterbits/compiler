package com.neaterbits.compiler.ast.variables;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.expression.FieldAccess;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

public final class FieldAccessReference extends VariableReference {

	private final ASTSingle<FieldAccess> expression;
	
	public FieldAccessReference(Context context, FieldAccess expression) {
		super(context);

		Objects.requireNonNull(expression);
		
		this.expression = makeSingle(expression);
	}
	
	public FieldAccess getExpression() {
		return expression.get();
	}
	
	@Override
	public TypeReference getType() {
		return expression.get().getType();
	}

	@Override
	public <T, R> R visit(VariableReferenceVisitor<T, R> visitor, T param) {
		return visitor.onFieldAccessReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(expression, recurseMode, iterator);
	}
}


