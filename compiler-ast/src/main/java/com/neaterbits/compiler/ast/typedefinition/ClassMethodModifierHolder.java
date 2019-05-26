package com.neaterbits.compiler.ast.typedefinition;

import com.neaterbits.compiler.util.Context;
import com.neaterbits.compiler.util.model.ParseTreeElement;
import com.neaterbits.compiler.util.typedefinition.ClassMethodModifier;
import com.neaterbits.compiler.util.typedefinition.ClassMethodModifierVisitor;

public final class ClassMethodModifierHolder extends BaseModifierHolder<ClassMethodModifier>
			implements ClassMethodModifier {

	public ClassMethodModifierHolder(Context context, ClassMethodModifier delegate) {
		super(context, delegate);
	}

	@Override
	public ParseTreeElement getParseTreeElement() {
		return ParseTreeElement.CLASS_METHOD_MODIFIER_HOLDER;
	}

	@Override
	public <T, R> R visit(ClassMethodModifierVisitor<T, R> visitor, T param) {
		return getDelegate().visit(visitor, param);
	}
}
