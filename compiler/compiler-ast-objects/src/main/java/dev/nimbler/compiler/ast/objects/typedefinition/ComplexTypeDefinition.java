package dev.nimbler.compiler.ast.objects.typedefinition;

import java.util.List;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.Keyword;
import dev.nimbler.compiler.ast.objects.list.ASTList;
import dev.nimbler.compiler.util.name.BaseTypeName;

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
