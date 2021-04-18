package dev.nimbler.compiler.convert.ootofunction;

import java.util.ArrayList;
import java.util.List;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.block.Block;
import dev.nimbler.compiler.ast.objects.block.Parameter;
import dev.nimbler.compiler.ast.objects.expression.ArrayAccessExpression;
import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.expression.literal.IntegerLiteral;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.ast.objects.statement.Statement;
import dev.nimbler.compiler.ast.objects.type.BaseType;
import dev.nimbler.compiler.ast.objects.type.primitive.BuiltinType;
import dev.nimbler.compiler.ast.objects.typedefinition.VariableModifierHolder;
import dev.nimbler.compiler.ast.objects.typedefinition.VariableModifiers;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.variables.NameReference;
import dev.nimbler.compiler.ast.objects.variables.VariableReference;
import dev.nimbler.compiler.convert.ConverterState;

public abstract class BaseConverter<T extends ConverterState<T>> {

	protected final TypeReference mapComplexType(TypeReference typeReference, T state) {
		return state.convertTypeReference(typeReference);
	}

	protected final TypeReference convertType(TypeReference typeReference, T state) {
		return state.convertTypeReference(typeReference);
	}

	protected final BuiltinType convertBuiltinType(TypeReference typeReference, T state) {
		throw new UnsupportedOperationException();
		// return ((BuiltinTypeReference)typeReference).getBuiltinType();
	}

	protected final BaseType convertType(BaseType type, T state) {
		return state.convertType(type);
	}

	protected Statement convertStatement(Statement statement, T state) {
		return state.convertStatement(statement);
	}

	protected Expression convertExpression(Expression expression, T state) {
		return state.convertExpression(expression);
	}

	protected final List<Expression> convertExpressions(ASTList<Expression> expressions, T state) {

		final List<Expression> converted = new ArrayList<>(expressions.size());

		for (Expression expression : expressions) {
			converted.add(convertExpression(expression, state));
		}

		return converted;
	}

	protected final ArrayAccessExpression makeStaticArrayAccess(Context context, String arrayName, int index, T state) {
		final IntegerLiteral indexLiteral = new IntegerLiteral(
				context,
				index,
				false,
				32,
				state.getIntType(),
				-1);

		final ArrayAccessExpression arrayAccessExpression = new ArrayAccessExpression(
				context,
				new NameReference(
						context,
						arrayName),
				indexLiteral);

		return arrayAccessExpression;
	}

	protected VariableModifiers convertModifiers(VariableModifiers modifiers) {

		final List<VariableModifierHolder> list = new ArrayList<>(modifiers.getModifiers().size());

		for (VariableModifierHolder modifierHolder : modifiers.getModifiers()) {
			list.add(new VariableModifierHolder(modifierHolder.getContext(), modifierHolder.getModifier()));
		}

		return new VariableModifiers(null, list);
	}

	protected final VariableReference convertVariableReference(VariableReference variableReference, T state) {
		return state.convertVariableReference(variableReference);
	}

	protected final Block convertBlock(Block block, T state) {

		final List<Statement> convertedStatements = new ArrayList<>(block.getStatements().size());

		for (Statement statement : block.getStatements()) {
			final Statement convertedStatement = convertStatement(statement, state);

			System.out.println("## converted statement " + statement + " to " + convertedStatement);

			if (convertedStatement != null) {
				convertedStatements.add(convertedStatement);
			}
		}

		return new Block(block.getContext(), convertedStatements);
	}

	protected final List<Parameter> convertParameters(ASTList<Parameter> parameters, java.util.function.Function<TypeReference, TypeReference> convertType) {

		final List<Parameter> result = new ArrayList<>(parameters.size());

		for (Parameter parameter : parameters) {
			result.add(new Parameter(
					parameter.getContext(),
					null,
					convertType.apply(parameter.getType()),
					parameter.getName(),
					parameter.isVarArgs()));
		}

		return result;
	}
}
