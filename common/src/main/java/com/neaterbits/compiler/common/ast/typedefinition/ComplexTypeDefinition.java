package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.list.ASTList;

public abstract class ComplexTypeDefinition extends TypeDefinition {

	private final ASTList<ComplexMemberDefinition> members;
	
	protected ComplexTypeDefinition(Context context, DefinitionName name, List<ComplexMemberDefinition> members) {
		super(context, name);
		
		this.members = makeList(members);
	}

	public final ASTList<ComplexMemberDefinition> getMembers() {
		return members;
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		doIterate(members, recurseMode, visitor);
	}
}
