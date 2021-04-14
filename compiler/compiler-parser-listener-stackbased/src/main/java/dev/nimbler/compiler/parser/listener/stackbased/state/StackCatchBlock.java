package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.parser.listener.stackbased.state.setters.TypeReferenceSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

public class StackCatchBlock<STATEMENT, TYPE_REFERENCE>
		extends StackStatements<STATEMENT>
		implements VariableNameSetter, TypeReferenceSetter<TYPE_REFERENCE> {

	private final List<TYPE_REFERENCE> exceptionTypes;
	private String exceptionVarName;
	
	public StackCatchBlock(ParseLogger parseLogger) {
		super(parseLogger);
		
		this.exceptionTypes = new ArrayList<>();
	}
	
	public void addExceptionType(TYPE_REFERENCE exceptionType) {
		Objects.requireNonNull(exceptionType);
		
		exceptionTypes.add(exceptionType);
	}
	
	public List<TYPE_REFERENCE> getExceptionTypes() {
		return exceptionTypes;
	}

	public String getExceptionVarName() {
		return exceptionVarName;
	}

	public void setExceptionVarName(String exceptionVarName) {
		
		Objects.requireNonNull(exceptionVarName);

		this.exceptionVarName = exceptionVarName;
	}

	@Override
	public void setTypeReference(TYPE_REFERENCE typeReference) {
		addExceptionType(typeReference);
	}

	@Override
	public void init(String name, Context nameContext, int numDims) {
		setExceptionVarName(name);
	}
}
