package com.neaterbits.compiler.ast.objects.typedefinition;

import java.util.List;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.Keyword;
import com.neaterbits.compiler.ast.objects.list.ASTList;
import com.neaterbits.compiler.util.name.BaseTypeName;
import com.neaterbits.util.parse.context.Context;

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
