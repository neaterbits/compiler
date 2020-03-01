package com.neaterbits.compiler.ast.objects.typedefinition;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.typedefinition.InterfaceMethodModifier;
import com.neaterbits.compiler.util.typedefinition.InterfaceMethodModifierVisitor;

public final class InterfaceMethodModifierHolder extends BaseModifierHolder<InterfaceMethodModifier> implements InterfaceMethodModifier {

	public InterfaceMethodModifierHolder(Context context, InterfaceMethodModifier delegate) {
		super(context, delegate);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.INTERFACE_METHOD_MODIFIER_HOLDER;
	}

	@Override
	public <T, R> R visit(InterfaceMethodModifierVisitor<T, R> visitor, T param) {
		return getDelegate().visit(visitor, param);
	}
}
