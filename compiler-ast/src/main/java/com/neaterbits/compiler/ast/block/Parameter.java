package com.neaterbits.compiler.ast.block;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.Name;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.typereference.TypeReference;
import com.neaterbits.compiler.util.Context;

public final class Parameter extends BaseASTElement {

	private final ASTSingle<TypeReference> type;
	private final ParameterName name;
	private final boolean varArgs;

	public Parameter(Context context, TypeReference type, ParameterName name, boolean varArgs) {
		super(context);

		Objects.requireNonNull(type);
		Objects.requireNonNull(name);
		
		Name.check(name.getName());

		this.type = makeSingle(type);
		this.name = name;
		this.varArgs = varArgs;
	}
	
	public TypeReference getType() {
		return type.get();
	}
	
	public ParameterName getName() {
		return name;
	}

	public boolean isVarArgs() {
		return varArgs;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(type, recurseMode, iterator);
	}
}