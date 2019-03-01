package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.list.ASTList;
import com.neaterbits.compiler.common.ast.type.BaseTypeName;

public abstract class ComplexTypeDefinition<T extends BaseTypeName, DECLARATION_NAME extends DeclarationName<T>>
		extends TypeDefinition<T, DECLARATION_NAME> {

	private final ASTList<ComplexMemberDefinition> members;
	
	protected ComplexTypeDefinition(Context context, DECLARATION_NAME name, List<ComplexMemberDefinition> members) {
		super(context, name);
		
		this.members = makeList(members);
	}

	public final ASTList<ComplexMemberDefinition> getMembers() {
		return members;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		doIterate(members, recurseMode, iterator);
	}
}
