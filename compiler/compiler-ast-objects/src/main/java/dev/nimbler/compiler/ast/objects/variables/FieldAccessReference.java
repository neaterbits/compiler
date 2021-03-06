package dev.nimbler.compiler.ast.objects.variables;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.expression.FieldAccess;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.types.ParseTreeElement;

public final class FieldAccessReference extends VariableReference {

	private final ASTSingle<FieldAccess> expression;
	
	public FieldAccessReference(Context context, FieldAccess expression) {
		super(context);

		Objects.requireNonNull(expression);
		
		this.expression = makeSingle(expression);
	}
	
	public FieldAccess getExpression() {
		return expression.get();
	}
	
	@Override
	public TypeReference getType() {
		return expression.get().getType();
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.FIELD_ACCESS_REFERENCE;
	}

	@Override
	public <T, R> R visit(VariableReferenceVisitor<T, R> visitor, T param) {
		return visitor.onFieldAccessReference(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(expression, recurseMode, iterator);
	}
}


