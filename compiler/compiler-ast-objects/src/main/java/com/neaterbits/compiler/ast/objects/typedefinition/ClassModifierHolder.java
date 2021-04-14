package com.neaterbits.compiler.ast.objects.typedefinition;

import com.neaterbits.compiler.types.ParseTreeElement;
import com.neaterbits.compiler.types.typedefinition.ClassModifier;
import com.neaterbits.compiler.types.typedefinition.ClassModifierVisitor;
import com.neaterbits.util.parse.context.Context;

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
