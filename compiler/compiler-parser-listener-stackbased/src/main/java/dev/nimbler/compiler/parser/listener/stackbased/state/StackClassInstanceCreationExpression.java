package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.StackEntry;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.ClassMethodMemberSetter;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.ParametersSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

public final class StackClassInstanceCreationExpression<TYPE_REFERENCE, CONSTRUCTOR_NAME, EXPRESSION, CLASS_METHOD_MEMBER>
	extends StackEntry implements ParametersSetter<EXPRESSION>, ClassMethodMemberSetter<CLASS_METHOD_MEMBER> {

	private TYPE_REFERENCE type;
	private CONSTRUCTOR_NAME constructorName;
	private List<EXPRESSION> parameters;
	
	// Anonymous classes
	private final List<CLASS_METHOD_MEMBER> methods;
	
	public StackClassInstanceCreationExpression(ParseLogger parseLogger) {
		super(parseLogger);
		
		this.methods = new ArrayList<>();
	}

	public TYPE_REFERENCE getType() {
		return type;
	}

	public void setType(TYPE_REFERENCE type) {
		
		Objects.requireNonNull(type);
		
		this.type = type;
	}

	public CONSTRUCTOR_NAME getConstructorName() {
		return constructorName;
	}

	public void setConstructorName(CONSTRUCTOR_NAME constructorName) {
		
		Objects.requireNonNull(constructorName);
		
		this.constructorName = constructorName;
	}

	public List<EXPRESSION> getParameters() {
		return parameters;
	}

	@Override
	public void setParameters(List<EXPRESSION> parameters) {
		
		Objects.requireNonNull(parameters);
		
		if (this.parameters != null) {
			throw new IllegalStateException("Parameters already set");
		}

		this.parameters = parameters;
	}

	public List<CLASS_METHOD_MEMBER> getMethods() {
		return methods;
	}

	@Override
	public void addMethod(CLASS_METHOD_MEMBER method) {
		Objects.requireNonNull(method);

		methods.add(method);
	}
}
