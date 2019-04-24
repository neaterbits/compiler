package com.neaterbits.compiler.ast.expression;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.expression.literal.Primary;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.type.complex.ComplexType;
import com.neaterbits.compiler.ast.type.primitive.ArrayType;
import com.neaterbits.compiler.ast.typereference.ComplexTypeReference;
import com.neaterbits.compiler.ast.typereference.ResolvedNamedTypeReference;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

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

	@SuppressWarnings("null")
	@Override
	public TypeReference getType() {
				
		@SuppressWarnings("unused")
		final ResolvedNamedTypeReference typeRef = (ResolvedNamedTypeReference)array.get().getType();
		final ArrayType arrayType = null; // (ArrayType)typeRef.getNamedType();

		return new ComplexTypeReference(getContext(), (ComplexType<?, ?, ?>)arrayType.getElementType());
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
