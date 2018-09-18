package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.list.ASTList;

public final class FieldModifiers extends BaseASTElement {
	
	private final ASTList<FieldModifierHolder> modifiers;

	public FieldModifiers(Context context, List<FieldModifierHolder> modifiers) {
		super(context);
		
		this.modifiers = makeList(modifiers);
	}

	public ASTList<FieldModifierHolder> getModifiers() {
		return modifiers;
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		doIterate(modifiers, recurseMode, visitor);
	}
}
