package com.neaterbits.compiler.common.ast.expression.literal;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.expression.ExpressionVisitor;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.typedefinition.ClassOrInterfaceName;

public final class ClassExpression extends Primary {

	private final ClassOrInterfaceName name;
	private final int numArrayDims;
	public ClassExpression(Context context, ClassOrInterfaceName name, int numArrayDims) {

		super(context);

		Objects.requireNonNull(name);
		
		this.name = name;
		this.numArrayDims = numArrayDims;
	}

	public ClassOrInterfaceName getName() {
		return name;
	}

	public int getNumArrayDims() {
		return numArrayDims;
	}

	@Override
	public BaseType getType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onClassExpression(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
