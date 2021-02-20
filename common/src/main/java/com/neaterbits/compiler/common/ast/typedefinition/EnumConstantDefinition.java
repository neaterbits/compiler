package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.CompilationCode;
import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.common.ast.expression.ParameterList;
import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

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
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {

		doIterate(members, recurseMode, visitor);
	}
}
