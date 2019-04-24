package com.neaterbits.compiler.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

public final class CastExpression extends Expression {

	private final ASTSingle<TypeReference> castType;
	private final ASTSingle<Expression> expession;
	
	public CastExpression(Context context, TypeReference castType, Expression expression) {
		super(context);
	
		Objects.requireNonNull(castType);
		Objects.requireNonNull(expression);
		
		this.castType = makeSingle(castType);
		this.expession = makeSingle(expression);
	}

	public TypeReference getCastType() {
		return castType.get();
	}

	public Expression getExpression() {
		return expession.get();
	}
	
	@Override
	public TypeReference getType() {
		return castType.get();
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onCastExpression(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(castType, recurseMode, iterator);
		doIterate(expession, recurseMode, iterator);
	}
}
