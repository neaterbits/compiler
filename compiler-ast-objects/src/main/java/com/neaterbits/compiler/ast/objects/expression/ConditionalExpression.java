package com.neaterbits.compiler.ast.objects.expression;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;

public final class ConditionalExpression extends Expression {

	private final ASTSingle<Expression> part1;
	private final ASTSingle<Expression> part2;
	private final ASTSingle<Expression> part3;
	
	
	public ConditionalExpression(Context context, Expression part1, Expression part2, Expression part3) {
		super(context);

		Objects.requireNonNull(part1);
		Objects.requireNonNull(part2);
		Objects.requireNonNull(part3);

		this.part1 = makeSingle(part1);
		this.part2 = makeSingle(part2);
		this.part3 = makeSingle(part3);
	}

	public Expression getPart1() {
		return part1.get();
	}

	public Expression getPart2() {
		return part2.get();
	}

	public Expression getPart3() {
		return part3.get();
	}
	
	@Override
	public TypeReference getType() {
		
		final TypeReference type1 = part2.get().getType();
		final TypeReference type2 = part3.get().getType();
		
		if (!type1.equals(type2)) {
			throw new IllegalStateException();
		}
		
		return type1;
	}
	
	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CONDITIONAL_EXPRESSION;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onConditionalExpression(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(part1, recurseMode, iterator);
		doIterate(part2, recurseMode, iterator);
		doIterate(part3, recurseMode, iterator);
	}
}
