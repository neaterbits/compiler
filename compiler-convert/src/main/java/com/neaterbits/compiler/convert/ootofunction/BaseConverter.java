package com.neaterbits.compiler.convert.ootofunction;

import java.util.ArrayList;
import java.util.List;

import com.neaterbits.compiler.ast.block.Block;
import com.neaterbits.compiler.ast.block.Parameter;
import com.neaterbits.compiler.ast.expression.ArrayAccessExpression;
import com.neaterbits.compiler.ast.expression.Expression;
import com.neaterbits.compiler.ast.expression.literal.IntegerLiteral;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.ast.statement.Statement;
import com.neaterbits.compiler.ast.type.BaseType;
import com.neaterbits.compiler.ast.type.primitive.BuiltinType;
import com.neaterbits.compiler.ast.typedefinition.VariableModifierHolder;
import com.neaterbits.compiler.ast.typedefinition.VariableModifiers;
import com.neaterbits.compiler.ast.typereference.BuiltinTypeReference;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.ast.variables.NameReference;
import com.neaterbits.compiler.ast.variables.VariableDeclaration;
import com.neaterbits.compiler.ast.variables.VariableReference;
import com.neaterbits.compiler.convert.ConverterState;
import com.neaterbits.compiler.util.Context;

public abstract class BaseConverter<T extends ConverterState<T>> {

	protected final TypeReference mapComplexType(TypeReference typeReference, T state) {
		return state.convertTypeReference(typeReference);
	}

	protected final TypeReference convertType(TypeReference typeReference, T state) {
		return state.convertTypeReference(typeReference);
	}

	protected final BuiltinType convertBuiltinType(TypeReference typeReference, T state) {
		return ((BuiltinTypeReference)typeReference).getNamedType();
	}

	protected final BaseType convertType(BaseType type, T state) {
		return state.convertType(type);
	}

	protected final VariableDeclaration mapVariableDeclaration(VariableDeclaration variableDeclaration, T state) {
		return state.mapVariableDeclaration(variableDeclaration);
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
				state.getIntType());
		
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

		return new VariableModifiers(list);
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
					convertType.apply(parameter.getType()),
					parameter.getName(),
					parameter.isVarArgs()));
		}

		return result;
	}
}
