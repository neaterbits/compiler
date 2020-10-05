package com.neaterbits.compiler.ast.objects.typedefinition;


import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.types.typedefinition.InterfaceModifier;
import com.neaterbits.compiler.types.typedefinition.InterfaceModifierVisitor;
import com.neaterbits.util.parse.context.Context;

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
