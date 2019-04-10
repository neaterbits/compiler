package com.neaterbits.compiler.ast.variables;

import java.util.Objects;

import com.neaterbits.compiler.ast.ASTIterator;
import com.neaterbits.compiler.ast.ASTRecurseMode;
import com.neaterbits.compiler.ast.BaseASTElement;
import com.neaterbits.compiler.ast.Name;
import com.neaterbits.compiler.util.Context;

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
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {
		
	}
}
