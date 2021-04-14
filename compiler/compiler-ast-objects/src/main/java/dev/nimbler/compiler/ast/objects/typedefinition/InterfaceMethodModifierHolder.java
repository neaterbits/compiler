package dev.nimbler.compiler.ast.objects.typedefinition;

import com.neaterbits.util.parse.context.Context;

import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.typedefinition.InterfaceMethodModifier;
import dev.nimbler.compiler.types.typedefinition.InterfaceMethodModifierVisitor;

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
