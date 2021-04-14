package dev.nimbler.compiler.ast.objects.variables;

import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.expression.ArrayAccessExpression;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class ArrayAccessReference extends VariableReference {
	
	private final ArrayAccessExpression expression;
	
	public ArrayAccessReference(Context context, ArrayAccessExpression expression) {
		super(context);
		
		Objects.requireNonNull(expression);
		
		this.expression = expression;
	}
	
	public ArrayAccessExpression getExpression() {
		return expression;
	}
	
	@Override
	public TypeReference getType() {
		return expression.getType();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.ARRAY_ACCESS_REFERENCE;
	}

	@Override
	public <T, R> R visit(VariableReferenceVisitor<T, R> visitor, T param) {
		return visitor.onArrayAccessReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
