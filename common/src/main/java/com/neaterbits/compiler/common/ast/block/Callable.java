package com.neaterbits.compiler.common.ast.block;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.CompilationCode;

public abstract class Callable<NAME extends CallableName> extends CompilationCode  {

	private final TypeReference returnType;
	private final NAME name;
	private final List<Parameter> parameters;
	private final Block block;

	Callable(Context context, TypeReference returnType, NAME name, List<Parameter> parameters, Block block) {
		super(context);
		
		Objects.requireNonNull(name);
		Objects.requireNonNull(parameters);
		Objects.requireNonNull(block);

		this.returnType = returnType;
		this.name = name;
		this.parameters = Collections.unmodifiableList(parameters);
		this.block = block;
	}
	
	public final TypeReference getReturnType() {
		return returnType;
	}

	public final NAME getName() {
		return name;
	}

	public final Block getBlock() {
		return block;
	}

	public final List<Parameter> getParameters() {
		return parameters;
	}
}
