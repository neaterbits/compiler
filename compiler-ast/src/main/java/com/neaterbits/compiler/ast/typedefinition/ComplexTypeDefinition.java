package com.neaterbits.compiler.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.Keyword;
import com.neaterbits.compiler.ast.list.ASTList;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.name.BaseTypeName;

public abstract class ComplexTypeDefinition<T extends BaseTypeName, DECLARATION_NAME extends DeclarationName<T>>
		extends TypeDefinition<T, DECLARATION_NAME> {

	private final ASTList<ComplexMemberDefinition> members;
	
	protected ComplexTypeDefinition(Context context, Keyword typeKeyword, DECLARATION_NAME name, List<ComplexMemberDefinition> members) {
		super(context, typeKeyword, name);
		
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
