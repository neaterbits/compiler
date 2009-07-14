package com.neaterbits.compiler.common.convert;

import java.util.Objects;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.Namespace;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.ast.variables.VariableDeclaration;
import com.neaterbits.compiler.common.ast.variables.VariableReference;

public abstract class ConverterState<T extends ConverterState<T>> {

	private final StatementConverter<T> statementConverter;
	private final ExpressionConverter<T> expressionConverter;
	private final VariableReferenceConverter<T> variableReferenceConverter;
	private final TypeConverter typeConverter;

	private Namespace currentNamespace;

	public ConverterState(
			StatementConverter<T> statementConverter,
			ExpressionConverter<T> expressionConverter,
			VariableReferenceConverter<T> variableReferenceConverter,
			TypeConverter typeConverter) {
		
		Objects.requireNonNull(statementConverter);
		Objects.requireNonNull(expressionConverter);
		Objects.requireNonNull(variableReferenceConverter);
		Objects.requireNonNull(typeConverter);
		
		this.statementConverter = statementConverter;
		this.expressionConverter = expressionConverter;
		this.variableReferenceConverter = variableReferenceConverter;
		this.typeConverter = typeConverter;
	}
	
	@SuppressWarnings("unchecked")
	public final Statement convertStatement(Statement statement) {
		return statement.visit(statementConverter, (T)this);
	}
	
	@SuppressWarnings("unchecked")
	public final Expression convertExpression(Expression expression) {
		return expression.visit(expressionConverter, (T)this);
	}
	
	public final TypeReference convertTypeReference(TypeReference type) {
		return type.getType().visit(typeConverter, type.getContext());
	}

	public final VariableDeclaration mapVariableDeclaration(VariableDeclaration variableDeclaration) {
		throw new UnsupportedOperationException();
	}
	
	@SuppressWarnings("unchecked")
	public final VariableReference convertVariableReference(VariableReference reference) {
		return reference.visit(variableReferenceConverter, (T)this);
	}
	
	public final Namespace getCurrentNamespace() {
		return currentNamespace;
	}

	public final void setCurrentNamespace(Namespace currentNamespace) {
		this.currentNamespace = currentNamespace;
	}
}
