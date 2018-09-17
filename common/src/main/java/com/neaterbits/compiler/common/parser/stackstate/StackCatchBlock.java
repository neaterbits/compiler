package com.neaterbits.compiler.common.parser.stackstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.variables.VarName;
import com.neaterbits.compiler.common.log.ParseLogger;

public class StackCatchBlock extends StackStatements {

	private final List<TypeReference> exceptionTypes;
	private VarName exceptionVarName;
	
	public StackCatchBlock(ParseLogger parseLogger) {
		super(parseLogger);
		
		this.exceptionTypes = new ArrayList<>();
	}
	
	public void addExceptionType(TypeReference exceptionType) {
		Objects.requireNonNull(exceptionType);
	}
	
	public List<TypeReference> getExceptionTypes() {
		return exceptionTypes;
	}

	public VarName getExceptionVarName() {
		return exceptionVarName;
	}

	public void setExceptionVarName(VarName exceptionVarName) {
		
		Objects.requireNonNull(exceptionVarName);

		this.exceptionVarName = exceptionVarName;
	}
}
