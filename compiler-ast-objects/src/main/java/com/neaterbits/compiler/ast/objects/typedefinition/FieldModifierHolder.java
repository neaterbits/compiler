package com.neaterbits.compiler.ast.objects.typedefinition;

import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.types.typedefinition.FieldModifier;
import com.neaterbits.util.parse.context.Context;

public final class FieldModifierHolder extends BaseModifierHolder<FieldModifier> {

	public FieldModifierHolder(Context context, FieldModifier modifier) {
		super(context, modifier);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.FIELD_MODIFIER_HOLDER;
	}
}

