package com.neaterbits.compiler.common.convert;

import java.util.Objects;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.variables.VariableReference;

public class Converters<T extends ConverterState<T>> {
	private final StatementConverter<T> statementConverter;
	private final ExpressionConverter<T> expressionConverter;
	private final VariableReferenceConverter<T> variableReferenceConverter;
	private final TypeReferenceConverter<T> typeReferenceConverter;
	private final TypeConverter<T> typeConverter;

	public Converters(
			StatementConverter<T> statementConverter,
			ExpressionConverter<T> expressionConverter,
			VariableReferenceConverter<T> variableReferenceConverter,
			TypeReferenceConverter<T> typeReferenceConverter,
			TypeConverter<T> typeConverter) {
		
		Objects.requireNonNull(statementConverter);
		Objects.requireNonNull(expressionConverter);
		Objects.requireNonNull(variableReferenceConverter);
		Objects.requireNonNull(typeReferenceConverter);
		Objects.requireNonNull(typeConverter);
		
		this.statementConverter = statementConverter;
		this.expressionConverter = expressionConverter;
		this.variableReferenceConverter = variableReferenceConverter;
		this.typeReferenceConverter = typeReferenceConverter;
		this.typeConverter = typeConverter;
	}

	final Statement convertStatement(Statement statement, T param) {
		return statement.visit(statementConverter, param);
	}
	
	final Expression convertExpression(Expression expression, T param) {
		return expression.visit(expressionConverter, param);
	}

	final VariableReference convertVariableReference(VariableReference variableReference, T param) {
		return variableReference.visit(variableReferenceConverter, param);
	}
	
	final TypeReference convertTypeReference(TypeReference type, T param) {
		return type.visit(typeReferenceConverter, param);
	}

	public final BaseType convertType(BaseType type, T param) {
		return type.visit(typeConverter, param);
	}
}
