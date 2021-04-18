package dev.nimbler.compiler.parser.listener.stackbased.state;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.parser.listener.stackbased.state.base.NamedListStackEntry;
import dev.nimbler.compiler.parser.listener.stackbased.state.setters.StatementSetter;
import dev.nimbler.compiler.util.parse.ParseLogger;

public abstract class CallableStackEntry<STATEMENT, PARAMETER, TYPE_REFERENCE>
	extends NamedListStackEntry<STATEMENT>
	implements StatementSetter<STATEMENT> {
		
	private final List<PARAMETER> parameters;

	private TYPE_REFERENCE returnType;
	
    private final List<TYPE_REFERENCE> thrownExceptions;
	
	public CallableStackEntry(ParseLogger parseLogger) {
		super(parseLogger);

		this.parameters = new ArrayList<>();
        this.thrownExceptions = new ArrayList<>();
	}

	public CallableStackEntry(ParseLogger parseLogger, String name, Context nameContext) {
		super(parseLogger, name, nameContext);

		this.parameters = new ArrayList<>();
        this.thrownExceptions = new ArrayList<>();
	}

	public final TYPE_REFERENCE getReturnType() {
		return returnType;
	}

	public final void setReturnType(TYPE_REFERENCE returnType) {
		this.returnType = returnType;
	}

	public final void addParameter(PARAMETER parameter) {
		Objects.requireNonNull(parameter);
		
		parameters.add(parameter);
	}

	public final List<PARAMETER> getParameters() {
		return parameters;
	}
	
	public final void addThrownException(TYPE_REFERENCE thrownException) {
	    
	    Objects.requireNonNull(thrownException);
	    
	    thrownExceptions.add(thrownException);
	}
	
	public final List<TYPE_REFERENCE> getThrownExceptions() {
        return thrownExceptions;
    }

    @Override
	public final void addStatement(STATEMENT statement) {
		super.add(statement);
	}
}
