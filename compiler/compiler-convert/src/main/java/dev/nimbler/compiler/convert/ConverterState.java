package dev.nimbler.compiler.convert;

import java.util.List;
import java.util.Objects;

import dev.nimbler.compiler.ast.objects.Namespace;
import dev.nimbler.compiler.ast.objects.expression.Expression;
import dev.nimbler.compiler.ast.objects.statement.Statement;
import dev.nimbler.compiler.ast.objects.type.BaseType;
import dev.nimbler.compiler.ast.objects.type.CompleteName;
import dev.nimbler.compiler.ast.objects.type.primitive.IntType;
import dev.nimbler.compiler.ast.objects.typereference.TypeReference;
import dev.nimbler.compiler.ast.objects.variables.VariableReference;
import dev.nimbler.compiler.util.name.BaseTypeName;
import dev.nimbler.compiler.util.name.DefinitionName;

public abstract class ConverterState<T extends ConverterState<T>> {

	public abstract IntType getIntType();

	private final Converters<T> converters;

	private Namespace currentNamespace;
	private List<DefinitionName> outerTypes;

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
	
	public final CompleteName makeCompleteName(BaseTypeName name) {
		
		Objects.requireNonNull(name);
		
		return new CompleteName(currentNamespace.getReference(), outerTypes, name);
	}
}
