package dev.nimbler.compiler.ast.objects.typedefinition;


import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.typedefinition.InterfaceModifier;
import dev.nimbler.compiler.types.typedefinition.InterfaceModifierVisitor;

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
