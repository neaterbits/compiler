package com.neaterbits.compiler.common.convert;

import java.util.Objects;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.Namespace;
import com.neaterbits.compiler.common.ast.expression.Expression;
import com.neaterbits.compiler.common.ast.statement.Statement;
import com.neaterbits.compiler.common.ast.type.BaseType;
import com.neaterbits.compiler.common.ast.variables.VariableDeclaration;
import com.neaterbits.compiler.common.ast.variables.VariableReference;

public abstract class ConverterState<T extends ConverterState<T>> {

	private final Converters<T> converters;

	private Namespace currentNamespace;

	public ConverterState(Converters<T> converters) {

		Objects.requireNonNull(converters);
		
		this.converters = converters;
	}
	
	@SuppressWarnings("unchecked")
	public final Statement convertStatement(Statement statement) {
		return converters.convertStatement(statement, (T)this);
	}
	
	@SuppressWarnings("unchecked")
	public final Expression convertExpression(Expression expression) {
		return converters.convertExpression(expression, (T)this);
	}
	
	@SuppressWarnings("unchecked")
	public final TypeReference convertTypeReference(TypeReference type) {
		return converters.convertTypeReference(type, (T)this);
	}

	@SuppressWarnings("unchecked")
	public final BaseType convertType(BaseType type) {
		return converters.convertType(type, (T)this);
	}
	
	public final VariableDeclaration mapVariableDeclaration(VariableDeclaration variableDeclaration) {
		throw new UnsupportedOperationException();
	}
	
	@SuppressWarnings("unchecked")
	public final VariableReference convertVariableReference(VariableReference reference) {
		return converters.convertVariableReference(reference, (T)this);
	}
	
	public final Namespace getCurrentNamespace() {
		return currentNamespace;
	}

	public final void setCurrentNamespace(Namespace currentNamespace) {
		this.currentNamespace = currentNamespace;
	}
}
