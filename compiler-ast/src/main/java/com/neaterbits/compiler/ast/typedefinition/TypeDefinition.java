package com.neaterbits.compiler.ast.typedefinition;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.CompilationCode;
import com.neaterbits.compiler.ast.Keyword;
import com.neaterbits.compiler.ast.list.ASTSingle;
import com.neaterbits.compiler.ast.type.BaseTypeName;
import com.neaterbits.compiler.util.Context;

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
