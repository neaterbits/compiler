package com.neaterbits.compiler.common.parser.stackstate;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.expression.ArrayAccessExpression;
import com.neaterbits.compiler.common.ast.expression.NestedExpression;
import com.neaterbits.compiler.common.ast.expression.PrimaryList;
import com.neaterbits.compiler.common.ast.expression.literal.Primary;
import com.neaterbits.compiler.common.ast.variables.ArrayAccessReference;
import com.neaterbits.compiler.common.ast.variables.PrimaryListVariableReference;
import com.neaterbits.compiler.common.ast.variables.VariableReference;
import com.neaterbits.compiler.common.log.ParseLogger;
import com.neaterbits.compiler.common.parser.ListStackEntry;
import com.neaterbits.compiler.common.parser.NestedExpressionSetter;
import com.neaterbits.compiler.common.parser.PrimarySetter;
import com.neaterbits.compiler.common.parser.VariableReferenceSetter;

public class StackPrimaryList extends ListStackEntry<Primary>
	implements PrimarySetter, VariableReferenceSetter, NestedExpressionSetter {

	public StackPrimaryList(ParseLogger parseLogger) {
		super(parseLogger);
	}

	public final Primary makePrimaryOrNull(Context context) {
		return getList().isEmpty()
				? null
				: makePrimary(context);
	}

	@Override
	public void setVariableReference(VariableReference variableReference) {
		addPrimary(variableReference);
	}

	@Override
	public final void addPrimary(Primary primary) {
		Objects.requireNonNull(primary);

		add(primary);
	}

	@Override
	public void addNestedExpression(NestedExpression expression) {
		Objects.requireNonNull(expression);
		
		add(expression);
	}

	public final Primary makePrimary(Context context) {
		
		final List<Primary> primaries = getList();
		
		final Primary result;
		
		if (primaries.isEmpty()) {
			throw new IllegalStateException("No expressions");
		}
		else if (primaries.size() == 1) {
			result = primaries.get(0);
		}
		else {
			result = new PrimaryList(context, primaries);
		}
		
		return result;
	}

	protected final VariableReference makeVariableReference(Context context) {

		final VariableReference variableReference;
		
		final List<Primary> primaries = getList();

		if (primaries.isEmpty()) {
			throw new IllegalStateException("No primaries");
		}
		else if (primaries.size() == 1) {
			final Primary primary = primaries.get(0);
			
			if (primary instanceof VariableReference) {
				variableReference = (VariableReference)primary;
			}
			else if (primary instanceof ArrayAccessExpression) {
				variableReference = new ArrayAccessReference(context, (ArrayAccessExpression)primary);
			}
			else {
				throw new UnsupportedOperationException("Unknown primary type " + primary.getClass().getSimpleName());
			}
		}
		else {
			variableReference = new PrimaryListVariableReference(context, new PrimaryList(context, primaries));
		}

		return variableReference;
	}
}
