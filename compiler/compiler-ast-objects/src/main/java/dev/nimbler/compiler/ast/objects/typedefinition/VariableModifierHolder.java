package dev.nimbler.compiler.ast.objects.typedefinition;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.typedefinition.VariableModifier;
import dev.nimbler.compiler.types.typedefinition.VariableModifierVisitor;

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
