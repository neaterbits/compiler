package com.neaterbits.compiler.ast.typedefinition;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.typedefinition.FieldModifier;

public final class FieldModifierHolder extends BaseModifierHolder<FieldModifier> {

	public FieldModifierHolder(Context context, FieldModifier modifier) {
		super(context, modifier);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.FIELD_MODIFIER_HOLDER;
	}
}

