package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;
import java.util.Objects;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.ASTVisitor;
import com.neaterbits.compiler.common.ast.BaseASTElement;
import com.neaterbits.compiler.common.ast.list.ASTList;

public final class InterfaceModifiers extends BaseASTElement {

	private final ASTList<InterfaceModifierHolder> modifiers;

	public InterfaceModifiers(Context context, List<InterfaceModifierHolder> modifiers) {
		super(context);
		
		Objects.requireNonNull(modifiers);

		this.modifiers = makeList(modifiers);
	}

	public ASTList<InterfaceModifierHolder> getModifiers() {
		return modifiers;
	}

	@Override
	public void doRecurse(ASTRecurseMode recurseMode, ASTVisitor visitor) {
		doIterate(modifiers, recurseMode, visitor);
	}
}
