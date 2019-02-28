package com.neaterbits.compiler.common.ast.expression;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.expression.literal.Primary;
import com.neaterbits.compiler.common.ast.list.ASTSingle;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.type.primitive.ArrayType;

public final class ArrayAccessExpression extends Primary {

	private final ASTSingle<Primary> array;
	private final ASTSingle<Expression> index;
	
	public ArrayAccessExpression(Context context, Primary array, Expression index) {
		super(context);

		this.array = makeSingle(array);
		this.index = makeSingle(index);
	}

	public Primary getArray() {
		return array.get();
	}

	public Expression getIndex() {
		return index.get();
	}

	@Override
	public BaseType getType() {
		final ArrayType arrayType = (ArrayType)array.get().getType();
		
		return arrayType.getElementType();
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {

		visitor.onArrayAccessExpression(this, param);
		
		return null;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(array, recurseMode, iterator);
		doIterate(index, recurseMode, iterator);
	}
}
