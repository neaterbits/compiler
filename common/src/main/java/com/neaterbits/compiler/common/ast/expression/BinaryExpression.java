package com.neaterbits.compiler.common.ast.expression;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public abstract class BinaryExpression extends Expression {

	private final ASTSingle<Expression> lhs;
	private final ASTSingle<Expression> rhs;
	
	public BinaryExpression(Context context, Expression lhs, Expression rhs) {
		super(context);
		
		Objects.requireNonNull(lhs);
		Objects.requireNonNull(rhs);
		
		this.lhs = makeSingle(lhs);
		this.rhs = makeSingle(rhs);
	}

	public final Expression getLhs() {
		return lhs.get();
	}

	public final Expression getRhs() {
		return rhs.get();
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		doIterate(lhs, recurseMode, visitor);
		doIterate(rhs, recurseMode, visitor);
	}
}
