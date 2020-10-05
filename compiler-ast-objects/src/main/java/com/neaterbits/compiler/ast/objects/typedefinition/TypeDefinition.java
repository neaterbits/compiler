package com.neaterbits.compiler.ast.objects.typedefinition;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.CompilationCode;
import com.neaterbits.compiler.ast.objects.Keyword;
import com.neaterbits.compiler.ast.objects.list.ASTSingle;
import com.neaterbits.compiler.util.name.BaseTypeName;
import com.neaterbits.util.parse.context.Context;

public abstract class TypeDefinition<T extends BaseTypeName, DECLARATION_NAME extends DeclarationName<T>> extends CompilationCode {
	
	private final ASTSingle<Keyword> typeKeyword;
	private final ASTSingle<DECLARATION_NAME> name;
	
	protected TypeDefinition(Context context, Keyword typeKeyword, DECLARATION_NAME name) {
		super(context);
		
		this.typeKeyword = typeKeyword != null ? makeSingle(typeKeyword) : null;
		this.name = makeSingle(name);
	}

	public final Keyword getTypeKeyword() {
		return typeKeyword.get();
	}

	public final DECLARATION_NAME getName() {
		return name.get();
	}

	public final BaseTypeName getTypeName() {
		return name.get().getName();
	}
	
	public final String getNameString() {
		return name.get().getName().getName();
	}
	
	protected final void doIterateTypeKeywordAndName(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
		if (typeKeyword != null) {
			doIterate(typeKeyword, recurseMode, iterator);
		}

		doIterate(name, recurseMode, iterator);
	}
}
