package com.neaterbits.compiler.ast.expression;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.expression.literal.Primary;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

public final class ArrayCreationExpression extends Primary {

	private final ASTSingle<TypeReference> type;
	private final ASTList<Expression> dimExpressions;
	private final int numDims;

	public ArrayCreationExpression(Context context, TypeReference type, List<Expression> dimExpressions, int numDims) {
		super(context);

		Objects.requireNonNull(type);
		Objects.requireNonNull(dimExpressions);
		
		this.type = makeSingle(type);
		this.dimExpressions = makeList(dimExpressions);
		this.numDims = numDims;
	}
	
	public TypeReference getTypeReference() {
		return type.get();
	}

	@Override
	public TypeReference getType() {
		return getTypeReference();
	}

	public ASTList<Expression> getDimExpressions() {
		return dimExpressions;
	}

	public int getNumDims() {
		return numDims;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		// TODO Auto-generated method stub
		
	}
}
