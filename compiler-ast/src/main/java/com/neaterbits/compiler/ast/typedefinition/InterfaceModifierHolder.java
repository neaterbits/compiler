package com.neaterbits.compiler.ast.typedefinition;


import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.typedefinition.InterfaceModifier;
import com.neaterbits.compiler.util.typedefinition.InterfaceModifierVisitor;

public final class InterfaceModifierHolder extends BaseModifierHolder<InterfaceModifier> implements InterfaceModifier {

	public InterfaceModifierHolder(Context context, InterfaceModifier modifier) {
		super(context, modifier);
	}
	
	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.INTERFACE_MODIFIER_HOLDER;
	}

	@Override
	public <T, R> R visit(InterfaceModifierVisitor<T, R> visitor, T param) {
		return getDelegate().visit(visitor, param);
	}
}
