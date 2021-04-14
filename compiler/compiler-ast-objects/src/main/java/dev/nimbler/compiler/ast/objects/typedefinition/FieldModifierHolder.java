package dev.nimbler.compiler.ast.objects.typedefinition;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.typedefinition.FieldModifier;

public final class FieldModifierHolder extends BaseModifierHolder<FieldModifier> {

	public FieldModifierHolder(Context context, FieldModifier modifier) {
		super(context, modifier);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.FIELD_MODIFIER_HOLDER;
	}
}

