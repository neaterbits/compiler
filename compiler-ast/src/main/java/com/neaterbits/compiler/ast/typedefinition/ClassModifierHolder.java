package com.neaterbits.compiler.ast.typedefinition;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.typedefinition.ClassModifier;
import com.neaterbits.compiler.util.typedefinition.ClassModifierVisitor;

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
