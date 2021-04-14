package dev.nimbler.compiler.ast.objects.expression;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.expression.literal.Primary;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.type.NamedType;
import dev.nimbler.compiler.ast.objects.type.primitive.ArrayType;
import dev.nimbler.compiler.ast.objects.typereference.ComplexTypeReference;
import dev.nimbler.compiler.ast.objects.typereference.ResolvedNamedTypeReference;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.variables.UnresolvedPrimary;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class ArrayAccessExpression extends UnresolvedPrimary {

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

		return new ComplexTypeReference(getContext(), -1, ((NamedType)arrayType.getElementType()).getTypeName());
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.ARRAY_ACCESS_EXPRESSION;
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
