package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.list.ASTList;

public final class ClassMethodModifiers extends BaseASTElement {

	private final ASTList<ClassMethodModifierHolder> modifiers;

	public ClassMethodModifiers(Context context, List<ClassMethodModifierHolder> modifiers) {
		super(context);

		this.modifiers = makeList(modifiers);
	}

	public ASTList<ClassMethodModifierHolder> getModifiers() {
		return modifiers;
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		doIterate(modifiers, recurseMode, visitor);
	}
}
