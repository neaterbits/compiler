package com.neaterbits.compiler.ast.objects.typedefinition;

import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.types.typedefinition.ConstructorModifier;
import com.neaterbits.compiler.types.typedefinition.ConstructorModifierVisitor;
import com.neaterbits.compiler.util.Context;

public final class ConstructorModifierHolder extends BaseModifierHolder<ConstructorModifier> implements ConstructorModifier {

	public ConstructorModifierHolder(Context context, ConstructorModifier modifier) {
		super(context, modifier);
	}
	
	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CONSTRUCTOR_MODIFIER_HOLDER;
	}

	@Override
	public <T, R> R visit(ConstructorModifierVisitor<T, R> visitor, T param) {
		return getDelegate().visit(visitor, param);
	}
}
