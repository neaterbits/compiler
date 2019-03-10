package com.neaterbits.compiler.ast.typedefinition;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.CompilationCode;
import com.neaterbits.compiler.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.ast.expression.ParameterList;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.util.Context;

public final class EnumConstantDefinition extends CompilationCode  {

	private final EnumConstantName name;
	private final ASTSingle<ParameterList> parameters;
	
	private final ASTList<ComplexMemberDefinition> members;
	
	public EnumConstantDefinition(Context context, EnumConstantName name, ParameterList parameters, List<ComplexMemberDefinition> members) {
		super(context);

		Objects.requireNonNull(name);
		Objects.requireNonNull(members);

		this.name = name;
		this.parameters = parameters != null ? makeSingle(parameters) : null;
		this.members = makeList(members);
	}

	public EnumConstantName getName() {
		return name;
	}

	public ParameterList getParameters() {
		return parameters.get();
	}

	public ASTList<ComplexMemberDefinition> getMembers() {
		return members;
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onEnumConstantDefinition(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(members, recurseMode, iterator);
	}
}
