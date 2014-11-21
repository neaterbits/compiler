package com.neaterbits.compiler.common.ast.expression;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.expression.literal.Primary;
import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.ast.list.ASTSingle;
import com.neaterbits.compiler.common.ast.type.BaseType;

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
	public BaseType getType() {
		return getTypeReference().getType();
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
