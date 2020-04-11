package com.neaterbits.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.parser.listener.stackbased.state.base.StackEntry;
import com.neaterbits.compiler.parser.listener.stackbased.state.setters.TypeReferenceSetter;
import com.neaterbits.compiler.util.parse.ParseLogger;

public final class StackArrayCreationExpression<TYPE_REFERENCE, EXPRESSION>
	extends StackEntry implements TypeReferenceSetter<TYPE_REFERENCE> {

	private TYPE_REFERENCE type;
	private final List<EXPRESSION> dimExpressions;
	private final int numDims;
	
	public StackArrayCreationExpression(ParseLogger parseLogger, TYPE_REFERENCE type, int numDims) {
		super(parseLogger);

		Objects.requireNonNull(type);

		this.type = type;
		this.dimExpressions = new ArrayList<>();
		this.numDims = numDims;
	}

	public TYPE_REFERENCE getType() {
		return type;
	}

	public List<EXPRESSION> getDimExpressions() {
		return dimExpressions;
	}

	public int getNumDims() {
		return numDims;
	}

	public void addDimExpression(EXPRESSION expression) {

		Objects.requireNonNull(expression);

		dimExpressions.add(expression);
	}

	@Override
	public void setTypeReference(TYPE_REFERENCE typeReference) {
		
		Objects.requireNonNull(typeReference);

		if (this.type != null) {
			throw new IllegalStateException();
		}
		
		this.type = typeReference;
	}
}
