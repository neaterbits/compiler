package dev.nimbler.compiler.ast.objects.typedefinition;

import java.util.Objects;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.ast.objects.ASTIterator;
import dev.nimbler.compiler.ast.objects.ASTRecurseMode;
import dev.nimbler.compiler.ast.objects.BaseASTElement;
import dev.nimbler.compiler.util.name.BaseTypeName;

public abstract class DeclarationName<T extends BaseTypeName> extends BaseASTElement {

	private final T name;

	public DeclarationName(Context context, T name) {
		
		super(context);
		
		Objects.requireNonNull(name);
		
		this.name = name;
	}

	public final T getName() {
		return name;
	}
	
	public final String getNameString() {
		return name.getName();
	}

	@Override
	protected final void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
