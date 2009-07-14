package com.neaterbits.compiler.common.convert.ootofunction;

import java.util.ArrayList;
import java.util.List;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.block.Block;
import com.neaterbits.compiler.common.ast.block.Parameter;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.ast.variables.VariableDeclaration;
import com.neaterbits.compiler.common.ast.variables.VariableReference;
import com.neaterbits.compiler.common.convert.ConverterState;

public abstract class BaseConverter<T extends ConverterState<T>> {

	protected final TypeReference mapComplexType(TypeReference typeReference, T state) {
		return state.convertTypeReference(typeReference);
	}

	protected final TypeReference convertType(TypeReference typeReference, T state) {
		return state.convertTypeReference(typeReference);
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
