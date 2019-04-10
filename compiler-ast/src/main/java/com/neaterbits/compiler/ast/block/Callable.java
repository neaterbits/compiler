package com.neaterbits.compiler.ast.block;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.CompilationCode;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

public abstract class Callable<NAME extends CallableName> extends CompilationCode {

	private final ASTSingle<TypeReference> returnType;
	private final ASTSingle<NAME> name;
	private final ASTList<Parameter> parameters;

	protected Callable(Context context, TypeReference returnType, NAME name, List<Parameter> parameters) {
		super(context);

		Objects.requireNonNull(name);
		Objects.requireNonNull(parameters);

		this.returnType = returnType != null ? makeSingle(returnType) : null;
		this.name = makeSingle(name);
		this.parameters = makeList(parameters);
	}

	public final TypeReference getReturnType() {
		return returnType != null ? returnType.get() : null;
	}

	public final NAME getName() {
		return name.get();
	}

	public final ASTList<Parameter> getParameters() {
		return parameters;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		if (returnType != null) {
			doIterate(returnType, recurseMode, iterator);
		}
		
		doIterate(name, recurseMode, iterator);

		doIterate(parameters, recurseMode, iterator);
	}
}
