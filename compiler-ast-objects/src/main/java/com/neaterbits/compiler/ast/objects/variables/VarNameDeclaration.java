package com.neaterbits.compiler.ast.objects.variables;

import java.util.Objects;

import com.neaterbits.compiler.ast.objects.ASTIterator;
import com.neaterbits.compiler.ast.objects.ASTRecurseMode;
import com.neaterbits.compiler.ast.objects.BaseASTElement;
import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.name.Name;

public final class VarNameDeclaration extends BaseASTElement {

	private final VarName varName;
	
	public VarNameDeclaration(Context context, String name) {
		super(context);

		Objects.requireNonNull(name);
		
		Name.check(name);
		
		this.varName = new VarName(name);
	}

	public VarName getVarName() {
		return varName;
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.VAR_NAME_DECLARATION;
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
