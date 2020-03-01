package com.neaterbits.compiler.ast.objects.typedefinition;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.typedefinition.VariableModifier;
import com.neaterbits.compiler.util.typedefinition.VariableModifierVisitor;

public final class VariableModifierHolder extends BaseModifierHolder<VariableModifier> implements VariableModifier {

	public VariableModifierHolder(Context context, VariableModifier delegate) {
		super(context, delegate);
	}

	@Override
	public <T, R> R visit(VariableModifierVisitor<T, R> visitor, T param) {
		return getDelegate().visit(visitor, param);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.VARIABLE_MODIFIER_HOLDER;
	}
}
