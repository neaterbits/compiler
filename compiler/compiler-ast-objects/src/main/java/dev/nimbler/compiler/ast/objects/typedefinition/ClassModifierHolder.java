package dev.nimbler.compiler.ast.objects.typedefinition;

import org.jutils.parse.context.Context;

import dev.nimbler.compiler.types.ParseTreeElement;
import dev.nimbler.compiler.types.typedefinition.ClassModifier;
import dev.nimbler.compiler.types.typedefinition.ClassModifierVisitor;

public final class ClassModifierHolder extends BaseModifierHolder<ClassModifier>
		implements ClassModifier {

	public ClassModifierHolder(Context context, ClassModifier delegate) {
		super(context, delegate);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CLASS_MODIFIER_HOLDER;
	}

	@Override
	public <T, R> R visit(ClassModifierVisitor<T, R> visitor, T param) {
		return getDelegate().visit(visitor, param);
	}
}
