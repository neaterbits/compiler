package com.neaterbits.compiler.common.ast.block;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.CompilationCode;
import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public abstract class Callable<NAME extends CallableName> extends CompilationCode {

	private final ASTSingle<TypeReference> returnType;
	private final NAME name;
	private final ASTList<Parameter> parameters;

	protected Callable(Context context, TypeReference returnType, NAME name, List<Parameter> parameters) {
		super(context);

		Objects.requireNonNull(name);
		Objects.requireNonNull(parameters);

		this.returnType = returnType != null ? makeSingle(returnType) : null;
		this.name = name;
		this.parameters = makeList(parameters);
	}

	public final TypeReference getReturnType() {
		return returnType != null ? returnType.get() : null;
	}

	public final NAME getName() {
		return name;
	}

	public final ASTList<Parameter> getParameters() {
		return parameters;
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		if (returnType != null) {
			doIterate(returnType, recurseMode, visitor);
		}

		doIterate(parameters, recurseMode, visitor);
	}
}
