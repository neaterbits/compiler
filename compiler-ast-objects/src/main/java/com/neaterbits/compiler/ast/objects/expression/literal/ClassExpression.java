package com.neaterbits.compiler.ast.objects.expression.literal;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.expression.ExpressionVisitor;
import com.neaterbits.compiler.ast.objects.typedefinition.ClassOrInterfaceName;
import com.neaterbits.compiler.ast.objects.typereference.TypeReference;
import com.neaterbits.compiler.ast.objects.variables.UnresolvedPrimary;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.util.parse.context.Context;

@Deprecated
public final class ClassExpression extends UnresolvedPrimary {

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
	public TypeReference getType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CLASS_EXPRESSION;
	}

	@Override
	public <T, R> R visit(ExpressionVisitor<T, R> visitor, T param) {
		return visitor.onClassExpression(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
