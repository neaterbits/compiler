package com.neaterbits.compiler.common.ast.block;

import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.TypeReference;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.Name;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public final class Parameter extends BaseASTElement {

	private final ASTSingle<TypeReference> type;
	private final ParameterName name;

	public Parameter(Context context, TypeReference type, ParameterName name) {
		super(context);

		Objects.requireNonNull(type);
		Objects.requireNonNull(name);
		
		Name.check(name.getName());

		this.type = makeSingle(type);
		this.name = name;
	}
	
	public TypeReference getType() {
		return type.get();
	}
	
	public ParameterName getName() {
		return name;
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		doIterate(type, recurseMode, visitor);
	}
}
