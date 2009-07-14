package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.BuiltinTypeReference;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.StackEntry;
import com.neaterbits.compiler.common.parser.TypeReferenceSetter;

public final class StackArrayCreationExpression extends StackEntry implements TypeReferenceSetter {

	private final TypeReference type;
	private final List<Expression> dimExpressions;
	private final int numDims;
	
	public StackArrayCreationExpression(ParseLogger parseLogger, TypeReference type, int numDims) {
		super(parseLogger);

		Objects.requireNonNull(type);

		this.type = type;
		this.dimExpressions = new ArrayList<>();
		this.numDims = numDims;
	}

	public TypeReference getType() {
		return type;
	}

	public List<Expression> getDimExpressions() {
		return dimExpressions;
	}

	public int getNumDims() {
		return numDims;
	}

	public void addDimExpression(Expression expression) {

		Objects.requireNonNull(expression);

		dimExpressions.add(expression);
	}

	@Override
	public void setTypeReference(TypeReference typeReference) {
		
		Objects.requireNonNull(typeReference);

		if (typeReference instanceof BuiltinTypeReference) {
			if (!typeReference.equals(type)) {
				throw new IllegalStateException("Type mismatch: " + type + "/" + typeReference);
			}
		}
		else {
			throw new UnsupportedOperationException();
		}
		
	}
}
