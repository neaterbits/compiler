package dev.nimbler.compiler.ast.objects.typedefinition;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.CompilationCode;
import dev.nimbler.compiler.ast.objects.Keyword;
import dev.nimbler.compiler.ast.objects.list.ASTSingle;
import dev.nimbler.compiler.util.name.BaseTypeName;

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
