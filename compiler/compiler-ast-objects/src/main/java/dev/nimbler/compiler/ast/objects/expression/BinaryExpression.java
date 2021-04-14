package dev.nimbler.compiler.ast.objects.expression;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;

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
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(lhs, recurseMode, iterator);
		doIterate(rhs, recurseMode, iterator);
	}
}
