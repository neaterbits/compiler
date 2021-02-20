package com.neaterbits.compiler.common.ast.typedefinition;

import java.util.List;

import com.neaterbits.compiler.common.Context;
import com.neaterbits.compiler.common.ast.ASTIterator;
import com.neaterbits.compiler.common.ast.ASTRecurseMode;
import com.neaterbits.compiler.common.ast.CompilationCodeVisitor;
import com.neaterbits.compiler.common.ast.list.ASTSingle;

public final class InterfaceDefinition extends ComplexTypeDefinition {

	private final ASTSingle<InterfaceModifiers> modifiers;

	public InterfaceDefinition(Context context, InterfaceModifiers modifiers, InterfaceName name, List<ComplexMemberDefinition> members) {
		super(context, name, members);

		this.modifiers = makeSingle(modifiers);
	}

	public InterfaceModifiers getModifiers() {
		return modifiers.get();
	}

	@Override
	public <T, R> R visit(CompilationCodeVisitor<T, R> visitor, T param) {
		return visitor.onInterfaceDefinition(this, param);
	}

	@Override
	protected void doRecurse(ASTRecurseMode recurseMode, ASTIterator iterator) {

		doIterate(modifiers, recurseMode, iterator);
		
		super.doRecurse(recurseMode, iterator);
	}
}
