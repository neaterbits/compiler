package dev.nimbler.compiler.ast.objects.expression.literal;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.expression.ExpressionVisitor;
import dev.nimbler.compiler.ast.objects.typedefinition.ClassOrInterfaceName;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.variables.UnresolvedPrimary;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class UnresolvedClassExpression extends UnresolvedPrimary {

	private final ClassOrInterfaceName name;
	private final int numArrayDims;
	
	public UnresolvedClassExpression(Context context, ClassOrInterfaceName name, int numArrayDims) {

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
	public TypeReference getType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.UNRESOLVED_CLASS_EXPRESSION;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onClassExpression(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
